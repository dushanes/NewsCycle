package com.newscycle.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newscycle.Article
import com.newscycle.BuildConfig
import com.newscycle.Feed
import com.newscycle.R
import com.newscycle.api.ApiUtilities
import com.newscycle.data.models.ArticleModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.article_card.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainRecyclerViewAdapter(
    private val FEED_TAG: Feed,
    recView: RecyclerView,
    val layoutManager: LinearLayoutManager,
    val query: String
) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    private val apiUtil: ApiUtilities by lazy { ApiUtilities }
    private val articles: ArrayList<ArticleModel> = ArrayList()
    private val cropOptions: RequestOptions = RequestOptions.centerCropTransform()
    private var pageNum = 1
    private var totalItemCount: Int
    private var lastVisibleItem: Int
    private var isLoading: Boolean = false
    private var curSort: String = ""
    private var curFromDate: String = ""

    init {
        refreshArticles(query)
        totalItemCount = layoutManager.itemCount
        lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        recView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isLoading && (visibleItemCount + 5) >= totalItemCount) {
                    isLoading = true
                    Log.d("INF", "is loading")
                    refreshArticles(query, curSort, curFromDate)
                }
            }
        })
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.article_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val cur = articles[pos]
        val calendar = Calendar.getInstance()
        calendar.time = cur.pubDate

        val _title = cutSource(cur.title, '-')
        holder.view.title.text = _title
        holder.view.desc.text = cur.desc
        holder.view.time.text = getTime(cur.pubDate)
        holder.view.source.text = cur.source.name

        holder.view.setOnClickListener {
            val intent = Intent(holder.view.context, Article::class.java)
            intent.putExtra("article", articles[pos])
            startActivity(holder.view.context, intent, null)
        }

        Glide.with(holder.view.context)
            .load(articles[pos].image)
            .apply(cropOptions)
            .thumbnail(0.5f)
            .into(holder.view.card_image)
    }

    private fun getTime(pubDate: Date): String? {
        return SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH).format(pubDate)
    }

    @SuppressLint("CheckResult")
    fun getQuery(query: String, sortBy: String, fromDate: String) {
        clear()
        pageNum = 1
        curSort = sortBy
        curFromDate = fromDate

        apiUtil.searchTopic(
            BuildConfig.NEWS_KEY,
            q = query,
            page = pageNum,
            fromDate = fromDate,
            sortBy = sortBy
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                articles.addAll(t.articles)
                Log.d(Feed.SEARCH_FEED.toString(), "Successful response, page $pageNum returned")
                pageNum++
                notifyDataSetChanged()
            }, { error ->
                error.message?.let { Log.d("Api Error:", it) }
            })
        notifyDataSetChanged()
    }

    @SuppressLint("CheckResult")
    fun refreshArticles(topic: String, sortBy: String = "", fromDate: String = "") {
        when (FEED_TAG) {
            Feed.MY_FEED -> {
                apiUtil.getCategoryHeadlines(
                    api_key = BuildConfig.NEWS_KEY,
                    page = pageNum,
                    category = "general"
                )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(
                            Feed.MY_FEED.toString(),
                            "Successful response, page $pageNum returned, Total articles: ${articles.size}"
                        )
                        pageNum++
                        notifyDataSetChanged()
                    }, { error ->
                        Log.d("Api Error:", error.message.toString())
                    })
            }
            Feed.POP_FEED -> {
                apiUtil.getTopHeadlines(BuildConfig.NEWS_KEY, page = pageNum)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { isLoading = false }
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(
                            Feed.POP_FEED.toString(),
                            "Successful response, page $pageNum returned, Total articles: ${articles.size}"
                        )
                        pageNum++
                        notifyDataSetChanged()
                    }, { error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })
            }
            Feed.TOPIC_FEED -> {
                apiUtil.getCategoryHeadlines(BuildConfig.NEWS_KEY, page = pageNum, category = topic)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { isLoading = false }
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(
                            Feed.TOPIC_FEED.toString(),
                            "Successful response, page $pageNum returned, Total articles: ${articles.size}"
                        )
                        pageNum++
                        notifyDataSetChanged()
                    }, { error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })
            }
            Feed.SEARCH_FEED -> {
                apiUtil.searchTopic(
                    BuildConfig.NEWS_KEY,
                    page = pageNum,
                    q = topic,
                    fromDate = fromDate,
                    sortBy = sortBy
                )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { isLoading = false }
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(
                            Feed.SEARCH_FEED.toString(),
                            "Successful response, page $pageNum returned, Total articles: ${articles.size}"
                        )
                        pageNum++
                        notifyDataSetChanged()
                    }, { error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })
            }
        }
    }

    private fun clear() {
        articles.clear()
        notifyDataSetChanged()
    }


    private fun cutSource(title: String?, remove: Char): String {
        if (title.isNullOrBlank()) return ""
        val size = title.length
        for (i in size - 1 downTo 0) {
            val char = title[i]
            if (char == remove) {
                val ret = title.removeRange(i, size)
                return ret
            }
        }
        return ""
    }
}
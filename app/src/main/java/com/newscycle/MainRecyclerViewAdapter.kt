package com.newscycle

import android.annotation.SuppressLint
import android.content.Context
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
import com.newscycle.api.ApiUtilities
import com.newscycle.api.ArticleModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.article_card.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

interface AdapterInterface {
    fun getQuery(query: String, sortBy: String, fromDate: String)
}

class MainRecyclerViewAdapter (private val context: Context,
                               private val FEED_TAG: String,
                               val recView: RecyclerView,
                               val layoutManager: LinearLayoutManager,
                               val query: String) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>(), AdapterInterface{

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

        recView.addOnScrollListener(object : RecyclerView.OnScrollListener() {  //TODO("Infinite scroll")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading) {
                    isLoading = true
                    if (visibleItemCount > (totalItemCount *.5) && firstVisibleItemPosition >= 0 && totalItemCount > 49 ) {
                        refreshArticles(query, curSort, curFromDate)
                    }
                }
                isLoading = false
            }
        })
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.article_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val cur = articles[pos]
        val calendar = Calendar.getInstance()
        calendar.time = cur.pubDate

        holder.view.title.text = cur.title
        holder.view.desc.text = cur.desc
        holder.view.time.text = getTime(cur.pubDate)
        holder.view.source.text = cur.source.name

        holder.view.setOnClickListener {
            val intent = Intent(context, Article::class.java)
            intent.putExtra("article", articles[pos])
            startActivity(context, intent, null)
        }

        Glide.with(context)
            .load(articles[pos].image)
            .apply(cropOptions)
            .thumbnail(0.5f)
            .into(holder.view.card_image)
    }

    fun getTime(pubDate: Date): String? {
        return SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH).format(pubDate)
    }

    override fun getQuery(query: String, sortBy: String, fromDate: String): Unit{
        clear()
        pageNum = 1
        curSort = sortBy
        curFromDate = fromDate

        apiUtil.searchTopic(BuildConfig.NEWS_KEY, q = query, page=pageNum, fromDate = fromDate, sortBy = sortBy)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                articles.addAll(t.articles)
                Log.d(Constants.SEARCH_FEED, "Successful response, page $pageNum returned")
                pageNum++
                notifyDataSetChanged()
            }, {error ->
                error.message?.let { Log.d("Api Error:", it) }
            })
        notifyDataSetChanged()
    }

    @SuppressLint("CheckResult")
    fun refreshArticles(topic: String, sortBy: String = "", fromDate: String = ""){
        when (FEED_TAG) {
            Constants.MY_FEED -> {
                apiUtil.getCategoryHeadlines(api_key = BuildConfig.NEWS_KEY, page = pageNum, category="general")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(Constants.MY_FEED, "Successful response, page $pageNum returned, Total articles: ${articles.size}")
                        pageNum++
                        notifyDataSetChanged()
                    }, {error ->
                        Log.d("Api Error:", error.message.toString())
                    })
            }
            Constants.POP_FEED -> {
                apiUtil.getTopHeadlines(BuildConfig.NEWS_KEY, page=pageNum)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(Constants.POP_FEED, "Successful response, page $pageNum returned, Total articles: ${articles.size}")
                        pageNum++
                        notifyDataSetChanged()
                    }, {error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })
            }
            Constants.TOPIC_FEED -> {
                apiUtil.getCategoryHeadlines(BuildConfig.NEWS_KEY, page=pageNum, category = topic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d( Constants.TOPIC_FEED, "Successful response, page $pageNum returned, Total articles: ${articles.size}")
                        pageNum++
                        notifyDataSetChanged()
                    }, {error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })
            }
            Constants.SEARCH_FEED -> {
                apiUtil.searchTopic(BuildConfig.NEWS_KEY, page=pageNum, q = topic, fromDate = fromDate,sortBy = sortBy)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(Constants.SEARCH_FEED , "Successful response, page $pageNum returned, Total articles: ${articles.size}")
                        pageNum++
                        notifyDataSetChanged()
                    }, {error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })
            }
        }
    }

    fun clear() {
        articles.clear()
        notifyDataSetChanged()
    }
}
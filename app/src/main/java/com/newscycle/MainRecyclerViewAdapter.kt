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

class MainRecyclerViewAdapter (private val context: Context,
                               private val FEED_TAG: String,
                               val recView: RecyclerView,
                               val layoutManager: LinearLayoutManager,
                               query: String) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>(){

    private val apiUtil: ApiUtilities by lazy { ApiUtilities }
    private val articles: ArrayList<ArticleModel> = ArrayList()
    private val cropOptions: RequestOptions = RequestOptions.centerCropTransform()
    private var pageNum = 1
    private var totalItemCount: Int
    private var lastVisibleItem: Int
    private var isLoading: Boolean = false

    init {
        refreshArticles(query)
        totalItemCount = layoutManager.itemCount
        lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        recView.addOnScrollListener(object : RecyclerView.OnScrollListener() {  //TODO("Infinite scroll")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading) {
                    isLoading = true
                    if (visibleItemCount > (totalItemCount *.5) && firstVisibleItemPosition >= 0 && totalItemCount >= 50) {
                        refreshArticles(query)
                    }
                }
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
            .into(holder.view.card_image)
    }

    fun getTime(pubDate: Date): String? {
        return SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH).format(pubDate)
    }

    @SuppressLint("CheckResult")
    fun refreshArticles(topic: String){
        when (FEED_TAG) {
            Constants.MY_FEED -> {
                apiUtil.getCategoryHeadlines(api_key = BuildConfig.NEWS_KEY, page = pageNum, category="general")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(Constants.MY_FEED, "Successful response, page $pageNum returned")
                        pageNum++
                        notifyDataSetChanged()
                    }, {error ->
                        Log.d("Api Error:", error.message)
                    }).also { isLoading = false } //TODO("Apart of implementing infinite scroll")
            }
            Constants.POP_FEED -> {
                apiUtil.getTopHeadlines(BuildConfig.NEWS_KEY, page=pageNum)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        pageNum++
                        Log.d("Popular Feed Api Response", "Successful response, page $pageNum returned")
                        notifyDataSetChanged()
                    }, {error ->
                        Log.d("Api Error:", error.message)
                    })
            }
            Constants.TOPIC_FEED -> {
                apiUtil.getCategoryHeadlines(BuildConfig.NEWS_KEY, page=pageNum, category = topic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ t ->
                        articles.addAll(t.articles)
                        pageNum++
                        Log.d("Topic Feed Api Response", "Successful response, page $pageNum returned")
                        notifyDataSetChanged()
                    }, {error ->
                        Log.d("Api Error:", error.message)
                    })
            }
        }
    }
}
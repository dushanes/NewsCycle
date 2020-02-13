package com.newscycle

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newscycle.api.ApiUtilities
import com.newscycle.api.Article
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.article_card.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainRecyclerViewAdapter (private val context: Context) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    private val apiUtil: ApiUtilities by lazy { ApiUtilities }
    private val articles: ArrayList<Article> = ArrayList()
   private val cropOptions: RequestOptions = RequestOptions.centerCropTransform()

    init {
        refreshArticles()
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
        holder.view.desc.movementMethod = ScrollingMovementMethod()

        holder.view.title.text = cur.title
        holder.view.desc.text = cur.desc
        holder.view.time.text = getTime(cur.pubDate)
        holder.view.source.text = cur.source.name

        Glide.with(context)
            .load(articles[pos].image)
            .apply(cropOptions)
            .into(holder.view.card_image)
    }

    private fun getTime(pubDate: Date): String? {
        return SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH).format(pubDate)
    }

    @SuppressLint("CheckResult")
    private fun refreshArticles(){
        apiUtil.getTopHeadlines(BuildConfig.NEWS_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
            articles.addAll(t.articles)
            notifyDataSetChanged()
        }, {error ->
            Log.d("Api Error:", error.message)
        })
    }


}
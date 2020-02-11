package com.newscycle

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newscycle.api.ApiUtilities
import com.newscycle.api.Article
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.article_card.view.*

class MainRecyclerViewAdapter : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    val apiUtil: ApiUtilities by lazy { ApiUtilities }
    val articles: ArrayList<Article> = ArrayList<Article>()

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
        holder.view.title.text = articles[pos].title
        holder.view.description.text = articles[pos].desc
        holder.view.time.text = articles[pos].pubDate
        holder.view.source.text = articles[pos].source.name
        holder.view.card_image.setImageURI(Uri.parse(articles[pos].image))
    }

    fun refreshArticles(){
        apiUtil.getTopHeadlines(BuildConfig.NEWS_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
            articles.addAll(t.articles)
        }, {error ->
            Log.d("Api Error:", error.message)
        })
    }
}
package com.newscycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainRecyclerViewAdapter() : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    val apiUtil: ApiUtilities by lazy {ApiUtilities}
    val articles: ArrayList<Article> = ArrayList<Article>()

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val viewImage: ImageView
        val viewTitle: TextView
        val viewDescription: TextView
        val viewDate: TextView
        val viewSource: TextView

        init{
            viewImage = view.findViewById(R.id.card_image)
            viewTitle = view.findViewById(R.id.title)
            viewDescription = view.findViewById(R.id.description)
            viewDate = view.findViewById(R.id.time)
            viewSource = view.findViewById(R.id.source)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.article_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        //To change body of created functions use File | Settings | File Templates.
    }

    fun refreshArticles(){
        apiUtil.getTopHeadlines(BuildConfig.NEWS_KEY).subscribe({t ->

        }, {error ->
            return@subscribe
        })
    }
}
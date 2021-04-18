package com.newscycle.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newscycle.Article
import com.newscycle.R
import com.newscycle.data.models.ArticleModel
import com.newscycle.databinding.ArticleCardBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticlesListPagingAdapter(val fragmentManager: FragmentManager) :
    PagingDataAdapter<ArticleModel, ArticlesListPagingAdapter.ViewHolder>(COMPARATOR) {
    private val cropOptions: RequestOptions = RequestOptions.centerCropTransform()

    class ViewHolder(val binding: ArticleCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ArticlesListPagingAdapter.ViewHolder, position: Int) {
        val cur = getItem(position)
        if (cur != null) {
            holder.binding.article = cur
            holder.binding.time.text = getFormattedTime(cur.pubDate)
            holder.binding.title.text = removeSource(cur.title, '-')
            holder.binding.cardView.setOnClickListener {
                val intent = Intent(holder.binding.root.context, Article::class.java)
                Article(cur).show(fragmentManager, "article opened: ${cur.title}")
            }
            Glide.with(holder.binding.root.context)
                .load(cur.image)
                .apply(cropOptions)
                .thumbnail(0.5f)
                .into(holder.binding.cardImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticlesListPagingAdapter.ViewHolder {
        val articleCardBinding: ArticleCardBinding = DataBindingUtil.inflate<ArticleCardBinding>(
            LayoutInflater.from(parent.context), R.layout.article_card, parent, false )
        //val v: View =LayoutInflater.from(parent.context).inflate(R.layout.article_card, parent, false)
        return ViewHolder(articleCardBinding)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }
        }
    }
    fun getFormattedTime(pubDate: Date): String? {
        return SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH).format(pubDate)
    }

    fun removeSource(title: String?, remove: Char): String {
        if (title.isNullOrBlank()) return ""
        val size = title.length
        for (i in size - 1 downTo 0) {
            val char = title[i]
            if (char == remove) {
                return title.removeRange(i, size)
            }
        }
        return ""
    }
}
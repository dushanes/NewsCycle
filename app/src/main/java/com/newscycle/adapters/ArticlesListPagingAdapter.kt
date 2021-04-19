package com.newscycle.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.newscycle.Article
import com.newscycle.R
import com.newscycle.data.models.ArticleModel
import com.newscycle.databinding.ArticleCardBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ArticlesListPagingAdapter(val fragmentManager: FragmentManager) :
    PagingDataAdapter<ArticleModel, ArticlesListPagingAdapter.ViewHolder>(COMPARATOR) {
    class ViewHolder(val binding: ArticleCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cur = getItem(position)
        if (cur != null) {
            holder.binding.article = cur
            holder.binding.time.text = getFormattedTime(cur.pubDate)
            holder.binding.title.text = removeSource(cur.title, '-')
            holder.binding.cardView.setOnClickListener {
                Article(cur).show(fragmentManager, "article opened: ${cur.title}")
            }
            Picasso.get()
                .load(cur.image)
                .centerCrop()
                .resize(600, 350)
                .into(holder.binding.cardImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
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
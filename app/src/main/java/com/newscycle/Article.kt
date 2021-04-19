package com.newscycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.newscycle.data.models.ArticleModel
import com.newscycle.databinding.FragmentArticleBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_article.*
import java.text.SimpleDateFormat
import java.util.*

class Article(private val article: ArticleModel) : DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentArticleBinding = DataBindingUtil.inflate<FragmentArticleBinding>(
            layoutInflater,
            R.layout.fragment_article, null, false
        )
        binding.article = article
        binding.articleTime.text = getFormattedTime(article.pubDate)

        lifecycleScope.launchWhenCreated {
            binding.articleTitle.text = removeSource(article.title, '-')
            if (article.content.isNullOrBlank()){
                binding.articleContent.visibility = View.GONE
            }else{
                binding.articleContent.text = removeSource(article.content, '[')
            }
        }

        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        binding.buttonVisitArticle.setOnClickListener {
            Toast.makeText(context, "Feature more involved than expected, coming soon...", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get()
            .load(article.image)
            .resize(2200,1200)
            .centerCrop()
            .into(article_image)
    }

    private fun removeSource(title: String?, remove: Char): String {
        if(title.isNullOrBlank()) return "Not available"
        val size = title.length
        for(i in size-1 downTo 0){
            val char = title[i]
            if(char == remove){
                val ret = title.removeRange(i, size)
                return ret
            }
        }
        return title
    }

    private fun getFormattedTime(pubDate: Date?): String {
        if (pubDate == null) return ""
        return SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH).format(pubDate)
    }
}
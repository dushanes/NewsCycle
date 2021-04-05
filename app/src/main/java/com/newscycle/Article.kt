package com.newscycle

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.newscycle.api.ArticleModel
import kotlinx.android.synthetic.main.activity_article.*
import java.text.SimpleDateFormat
import java.util.*

class Article : AppCompatActivity() {
    private lateinit var article: ArticleModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        if (intent != null && intent.extras != null) {
            article = intent.getParcelableExtra("article")!!
        } else {
            Log.d("Article", "No article object in extra")
        }

        article_title.text = article.title
        article_content.text = article.content
        article_time.text = getTime(article.pubDate)
        Glide.with(this)
            .load(article.image)
            .into(article_image)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    fun getTime(pubDate: Date?): String {
        if (pubDate == null) return ""
        return SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH).format(pubDate)
    }
}
package com.newscycle

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.net.URL

@JsonClass(generateAdapter = true)
data class Article(val source: Source,
              val author: String,
              val title: String,
              @Json(name= "description")val desc: String,
              val url: URL,
              @Json(name= "urlToImage")val image: URL,
              @Json(name= "publishedAt")val pubDate: String,
              val content: String)

@JsonClass(generateAdapter = true)
data class Source(val id: String, val name: String)

@JsonClass(generateAdapter = true)
data class Results (val status: String,
               @Json(name= "totalResults")val len: Int,
              val articles: ArrayList<Article>)
package com.newscycle.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Results (val status: String,
                    @Json(name= "totalResults")val len: Int,
                    val articles: List<Article>)

@JsonClass(generateAdapter = true)
data class Article(val source: Source,
                   val author: String? = null,
                   val title: String,
                   @Json(name= "description")val desc: String,
                   val url: String,
                   @Json(name= "urlToImage")val image: String,
                   @Json(name= "publishedAt")val pubDate: String,
                   val content: String? = null)

@JsonClass(generateAdapter = true)
data class Source(val id: String? = null, val name: String)

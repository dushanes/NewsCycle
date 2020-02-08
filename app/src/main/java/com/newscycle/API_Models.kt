package com.newscycle

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.net.URL

@JsonClass(generateAdapter = true)
class Article(source: Source,
              author: String,
              title: String,
              @Json(name= "description") desc: String,
              url: URL,
              @Json(name= "urlToImage") image: URL,
              @Json(name= "publishedAt") pubDate: String,
              content: String)

@JsonClass(generateAdapter = true)
class Source(id: String, name: String)

@JsonClass(generateAdapter = true)
class Results (status: String,
               @Json(name= "totalResults")len: Int,
              articles: ArrayList<Article>)
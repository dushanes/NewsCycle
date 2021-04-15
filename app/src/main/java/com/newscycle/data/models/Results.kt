package com.newscycle.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Results(
    val status: String,
    @Json(name = "totalResults") val len: Int,
    val articles: List<ArticleModel>
)
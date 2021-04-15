package com.newscycle.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourcesResult(
    val status: String,
    val sources: List<SourcesModel>
)
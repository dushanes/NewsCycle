package com.newscycle.data.models

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Source(val id: String? = null, val name: String?) : Serializable
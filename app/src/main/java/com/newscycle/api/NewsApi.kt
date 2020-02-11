package com.newscycle.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi {
    @GET("/v2/top-headlines")
    public fun getTopHeadlines(@Header("Authorization") api_key: String, @Query("country") country: String="us")
            :Single<Results>
}

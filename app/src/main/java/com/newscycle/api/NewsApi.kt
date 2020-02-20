package com.newscycle.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    fun getTopHeadlines(@Query("apiKey") api_key: String,
                       @Query("sortBy") sortBy: String="popularity",
                       @Query("page") page: Int,
                       @Query("pageSize") pageSize: Int = 50,
                       @Query("country") country: String="us")
            :Single<Results>

    @GET("/v2/top-headlines")
    fun getCategoryHeadlines(@Query("apiKey") api_key: String,
                       @Query("category") category: String,
                       @Query("sortBy") sortBy: String="popularity",
                       @Query("page") page: Int,
                       @Query("pageSize") pageSize: Int = 50,
                       @Query("country") country: String="us")
            :Single<Results>

    @GET("/v2/everything")
    fun searchTopic(@Query("apiKey") api_key: String,
                        @Query("q") q: String,
                        @Query("pageSize") pageSize: Int = 50,
                        @Query("page") page: Int)
            :Single<Results>
}

package com.newscycle.api

import com.newscycle.BuildConfig
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("/v2/sources")
    fun getSources(
        @Query("apiKey") api_key: String = BuildConfig.NEWS_KEY,
        @Query("category") category: String,
        @Query("language") language: String = "en"
    )
            : Single<SourcesResults>

    @GET("/v2/top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") api_key: String = BuildConfig.NEWS_KEY,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 50,
        @Query("country") country: String = "us"
    )
            : Single<Results>

    @GET("/v2/top-headlines")
    fun getCategoryHeadlines(
        @Query("apiKey") api_key: String = BuildConfig.NEWS_KEY,
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 50,
        @Query("country") country: String = "us"
    )
            : Single<Results>

    @GET("/v2/everything")
    fun searchTopic(
        @Query("apiKey") api_key: String = BuildConfig.NEWS_KEY,
        @Query("q") q: String,
        @Query("pageSize") pageSize: Int = 50,
        @Query("page") page: Int,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String = "PublishedAt",
        @Query("language") language: String = "en"
    )
            : Single<Results>

    @GET("/v2/everything")
    fun searchTopic(
        @Query("apiKey") api_key: String = BuildConfig.NEWS_KEY,
        @Query("q") q: String,
        @Query("pageSize") pageSize: Int = 50,
        @Query("page") page: Int,
        @Query("sortBy") sortBy: String = "PublishedAt",
        @Query("language") language: String = "en"
    )
            : Single<Results>
}

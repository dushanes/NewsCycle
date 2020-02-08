package com.newscycle

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface NewsApi {
    @GET("/v2/top-headlines")
    public fun getTopHeadlines(@Header("Authorization") api_key: String)
            :Single<Results>
}

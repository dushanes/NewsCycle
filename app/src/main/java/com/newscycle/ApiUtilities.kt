package com.newscycle

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiUtilities: NewsApi{
    val newsApi: NewsApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return@lazy retrofit.create(NewsApi::class.java)
    }

    override fun getTopHeadlines(api_key: String): Single<Results> {
        return newsApi.getTopHeadlines(BuildConfig.NEWS_KEY)
    }
}
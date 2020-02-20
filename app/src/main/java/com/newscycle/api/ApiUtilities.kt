package com.newscycle.api

import com.newscycle.BuildConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.net.URLEncoder
import java.util.*

object ApiUtilities: NewsApi {
    val newsApi: NewsApi by lazy {
        val moshi: Moshi = Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe()).build()
        val listArticle: Type = Types.newParameterizedType(List::class.java, Article::class.java)

        val adapter :JsonAdapter<List<Article>> = moshi.adapter(listArticle)
        val dateAdapter : JsonAdapter<Date> = moshi.adapter(Date::class.java)

        moshi.newBuilder()
            .add(listArticle, adapter)
            .add(Date::class.java, dateAdapter)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return@lazy retrofit.create(NewsApi::class.java)
    }

    override fun getTopHeadlines(api_key: String, sortBy: String, page: Int, pageSize: Int,country: String): Single<Results> {
        return newsApi.getTopHeadlines(
            BuildConfig.NEWS_KEY,
            page = page
        )
    }

    override fun getCategoryHeadlines(api_key: String, category: String, sortBy: String, page: Int, pageSize: Int, country: String): Single<Results> {
        return newsApi.getCategoryHeadlines(
            BuildConfig.NEWS_KEY,
            category= category,
            page = page
        )
    }

    override fun searchTopic( api_key: String, q: String, pageSize: Int, page: Int): Single<Results> {
        return newsApi.searchTopic(
            q= URLEncoder.encode(q),
            api_key= BuildConfig.NEWS_KEY,
            pageSize= pageSize,
            page= page
        )
    }
}
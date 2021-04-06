package com.newscycle.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

object ApiUtilities : NewsApi {
    val newsApi: NewsApi by lazy {
        val moshi: Moshi =
            Moshi.Builder().add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe()).build()
        val listArticle: Type =
            Types.newParameterizedType(List::class.java, ArticleModel::class.java)
        val adapter: JsonAdapter<List<ArticleModel>> = moshi.adapter(listArticle)
        val dateAdapter: JsonAdapter<Date> = moshi.adapter(Date::class.java)

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

    override fun getSources(
        api_key: String,
        category: String,
        language: String
    ): Single<SourcesResults> {
        return newsApi.getSources(category = category, language = "en")
    }

    override fun getTopHeadlines(
        api_key: String,
        page: Int,
        pageSize: Int,
        country: String
    ): Observable<Results> {
        return newsApi.getTopHeadlines(
            page = page
        )
    }

    override fun getCategoryHeadlines(
        api_key: String,
        category: String,
        page: Int,
        pageSize: Int,
        country: String
    ): Observable<Results> {
        return newsApi.getCategoryHeadlines(
            category = category,
            page = page
        )
    }

    override fun searchTopic(
        api_key: String,
        q: String,
        pageSize: Int,
        page: Int,
        fromDate: String,
        sortBy: String,
        language: String
    ): Observable<Results> {
        return newsApi.searchTopic(
            q = URLEncoder.encode(q),
            page = page,
            fromDate = getTime1806(fromDate),
            sortBy = sortBy
        )
    }

    private fun getTime1806(comp: String): String {
        if(comp.isNullOrBlank()) return  ""
        val cal: Calendar = Calendar.getInstance()
        var date: Date = cal.time
        when (comp) {
            "Today" -> {
                cal.add(Calendar.DATE, -1)
                date = cal.time
            }
            "Past Week" -> {
                cal.add(Calendar.DATE, -7)
                date = cal.time
            }
            "Past Month" -> {
                cal.add(Calendar.DATE, -30)
                date = cal.time
            }
        }
        val formattedDate: String =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH).format(date)
        return formattedDate
    }
}
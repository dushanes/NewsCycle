package com.newscycle.data

import android.annotation.SuppressLint
import com.newscycle.BuildConfig
import com.newscycle.Feed
import com.newscycle.api.ApiUtilities
import com.newscycle.data.models.Results
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ArticleListRepository(topic: String, sortBy: String, fromDate: String, private val FEED_TAG: Feed) {
    private val articleListLiveData: Observable<Results> = refreshArticles(topic, sortBy, fromDate)
    private val apiUtil: ApiUtilities by lazy { ApiUtilities }
    private var pageNum = 1
    private var isLoading: Boolean = false
    private var curSort: String = ""
    private var curFromDate: String = ""

    @SuppressLint("CheckResult")
    fun refreshArticles(topic: String, sortBy: String = "", fromDate: String = ""): Observable<Results> {
        return when (FEED_TAG) {
            Feed.MY_FEED -> {
                apiUtil.getCategoryHeadlines(
                    api_key = BuildConfig.NEWS_KEY,
                    page = pageNum,
                    category = "general"
                )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { pageNum++ }
                    /*.subscribe({ t ->
                        articleListLiveData.postValue()
                        Log.d(
                            Constants.MY_FEED,
                            "Successful response, page $pageNum returned, Total articles: ${articles.size}"
                        )
                        pageNum++
                    }, { error ->
                        Log.d("Api Error:", error.message.toString())
                    })*/
            }
            Feed.POP_FEED -> {
                apiUtil.getTopHeadlines(BuildConfig.NEWS_KEY, page = pageNum)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { pageNum++ }
                    .doOnComplete { isLoading = false }
                    /*.subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(
                            Constants.POP_FEED,
                            "Successful response, page $pageNum returned, Total articles: ${articles.size}"
                        )
                        pageNum++
                    }, { error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })*/
            }
            Feed.TOPIC_FEED -> {
                apiUtil.getCategoryHeadlines(BuildConfig.NEWS_KEY, page = pageNum, category = topic)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { pageNum++ }
                    .doOnComplete { isLoading = false }
                    /*.subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(
                            Constants.TOPIC_FEED,
                            "Successful response, page $pageNum returned, Total articles: ${articles.size}"
                        )
                        pageNum++
                    }, { error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })*/
            }
            Feed.SEARCH_FEED -> {
                apiUtil.searchTopic(
                    BuildConfig.NEWS_KEY,
                    page = pageNum,
                    q = topic,
                    fromDate = fromDate,
                    sortBy = sortBy
                )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { pageNum++ }
                    .doOnComplete { isLoading = false }
                    /*.subscribe({ t ->
                        articles.addAll(t.articles)
                        Log.d(
                            Constants.SEARCH_FEED,
                            "Successful response, page $pageNum returned, Total articles: ${articles.size}"
                        )
                        pageNum++
                    }, { error ->
                        error.message?.let { Log.d("Api Error:", it) }
                    })*/
            }
        }
    }

    fun getArticleListLiveData(): Observable<Results>{
        return articleListLiveData
    }
}
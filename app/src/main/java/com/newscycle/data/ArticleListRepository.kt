package com.newscycle.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.newscycle.Feed
import com.newscycle.api.ApiUtilities
import com.newscycle.data.models.ArticleModel
import com.newscycle.data.models.Results
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ArticleListRepository(private val FEED_TAG: Feed) {
    private val articleListLiveData: MutableLiveData<Results> = MutableLiveData()
    private val apiUtil: ApiUtilities by lazy { ApiUtilities }

    @ExperimentalCoroutinesApi
    fun getArticles(query: String, fromDate: String, sortBy: String): Flowable<PagingData<ArticleModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { ArticlePagingSource(query, sortBy, fromDate, FEED_TAG) }
        ).flowable
    }

    fun getArticleListLiveData(): MutableLiveData<Results> {
        return articleListLiveData
    }
}
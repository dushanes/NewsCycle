package com.newscycle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.newscycle.Feed
import com.newscycle.data.ArticleListRepository
import com.newscycle.data.models.ArticleModel
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ArticleListViewModel(FEED_TAG: Feed): ViewModel() {
    private var articleListRepository: ArticleListRepository = ArticleListRepository(FEED_TAG)
    private var currentSearchResult: Flowable<PagingData<ArticleModel>>?  =  null
    private var currentQueryValue: String?  =  null

    @ExperimentalCoroutinesApi
    fun getArticles(query: String, fromDate: String="", sortBy: String=""): Flowable<PagingData<ArticleModel>> {
        val lastResult = currentSearchResult
        if(query == currentQueryValue && lastResult != null){
            return lastResult
        }
        currentQueryValue = query
        val newResult: Flowable<PagingData<ArticleModel>> = articleListRepository.getArticles(query, fromDate, sortBy)
        currentSearchResult = newResult
        return newResult
    }
}
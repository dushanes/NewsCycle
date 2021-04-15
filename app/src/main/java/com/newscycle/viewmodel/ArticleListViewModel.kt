package com.newscycle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newscycle.Feed
import com.newscycle.data.ArticleListRepository
import com.newscycle.data.models.ArticleModel

class ArticleListViewModel(): ViewModel() {
    private val articleListLiveData: MutableLiveData<ArrayList<ArticleModel>> = MutableLiveData()
    private lateinit var articleList: ArrayList<ArticleModel>
    private lateinit var articleListRepository: ArticleListRepository

    fun getArticleListLiveData(): MutableLiveData<ArrayList<ArticleModel>>{
        return articleListLiveData
    }

    constructor(topic: String, fromDate: String, sortBy:String, FEED_TAG: Feed) : this() {
        this.articleListRepository = ArticleListRepository(topic, fromDate, sortBy, FEED_TAG)
        val disposable = articleListRepository.getArticleListLiveData().subscribe{
            articleList.addAll(it.articles)
            articleListLiveData.postValue(articleList)
        }
    }
}
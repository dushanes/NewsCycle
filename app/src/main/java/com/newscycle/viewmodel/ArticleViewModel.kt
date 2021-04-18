package com.newscycle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ArticleViewModel: ViewModel() {
    private val titleLiveData: MutableLiveData<String> = MutableLiveData()
    private val sourceLiveData: MutableLiveData<String> = MutableLiveData()
    private val authorLiveData: MutableLiveData<String> = MutableLiveData()
    private val descriptionLiveData: MutableLiveData<String> = MutableLiveData()
    private val urlLiveData: MutableLiveData<String> = MutableLiveData()
    private val imageLiveData: MutableLiveData<String> = MutableLiveData()
    private val dateLiveData: MutableLiveData<Date> = MutableLiveData()
    private val contentLiveData: MutableLiveData<String> = MutableLiveData()

    fun getTitleLiveData(): MutableLiveData<String>{
        return titleLiveData
    }

    fun getSourceLiveData(): MutableLiveData<String>{
        return sourceLiveData
    }

    fun getAuthorLiveData(): MutableLiveData<String> {
        return authorLiveData
    }

    fun getDescriptionLiveData(): MutableLiveData<String>{
        return descriptionLiveData
    }

    fun getImageLiveData(): MutableLiveData<String>{
        return imageLiveData
    }

    fun getDateLiveData(): MutableLiveData<Date>{
        return dateLiveData
    }

    fun getContentLiveData(): MutableLiveData<String>{
        return contentLiveData
    }



}
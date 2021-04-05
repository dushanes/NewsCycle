package com.newscycle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ArticleViewModel: ViewModel() {
    var titleLiveData: MutableLiveData<String> = MutableLiveData()
    var sourceLiveData: MutableLiveData<String> = MutableLiveData()
    var authorLiveData: MutableLiveData<String> = MutableLiveData()
    var descLiveData: MutableLiveData<String> = MutableLiveData()
    var urlLiveData: MutableLiveData<String> = MutableLiveData()
    var imageLiveData: MutableLiveData<String> = MutableLiveData()
    var DateLiveData: MutableLiveData<Date> = MutableLiveData()
    var contentLiveData: MutableLiveData<String> = MutableLiveData()
}
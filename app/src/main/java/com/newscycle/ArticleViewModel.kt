package com.newscycle

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ArticleViewModel(title: String): ViewModel(){
    private lateinit var title: MutableLiveData<String>
    private lateinit var content: MutableLiveData<String>
    private lateinit var date: MutableLiveData<Date>
    private lateinit var imageSRC: MutableLiveData<Uri>

    constructor(title: String,
                content: String,
                date: Date,
                imageSRC: Uri): this(title){
        this.title.postValue(title)
        this.content.postValue(content)
        this.date.postValue(date)
        this.imageSRC.postValue(imageSRC)
    }
}
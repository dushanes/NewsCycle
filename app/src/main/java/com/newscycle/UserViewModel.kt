package com.newscycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel(firstName: String): ViewModel() {
    private lateinit var firstname: MutableLiveData<String>
    private lateinit var email: MutableLiveData<String>

    constructor(firstname: String, email: String): this(firstname){
        this.firstname.postValue(firstname)
        this.email.postValue(email)
    }
}
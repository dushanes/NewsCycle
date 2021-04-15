package com.newscycle.viewmodel

import android.app.Application
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseUser
import com.newscycle.R
import com.newscycle.data.AuthRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepository(application)
    private val loggingInLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    val pass: MutableLiveData<String> = MutableLiveData()
    val passConfirm: MutableLiveData<String> = MutableLiveData()
    private val toastMsg: MutableLiveData<String> = MutableLiveData()
    private val errorMsg: String = "Email or Password is not valid"

    init {
        authRepository.getUserLiveData().observeForever {
            userLiveData.postValue(it)
        }
        authRepository.getLoggingInLiveData().observeForever {
            if (it != loggingInLiveData.value) loggingInLiveData.postValue(it)
        }
    }

    fun login(email: String?, pass: String?) {
        loggingInLiveData.value = true

        if (email.isNullOrBlank() || pass.isNullOrBlank()  || !isEmailValid(email)) {
            toastMsg.postValue(errorMsg)
            loggingInLiveData.value = false
            return
        }

        authRepository.onFireSignIn(email, pass)
    }

    fun toRegister(view: View) {
        (view.context as AppCompatActivity).findNavController(R.id.content_fragment_container)
            .navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun forgotPassword(email: String?) {
        if (email.isNullOrBlank() || !isEmailValid(email)) {
            toastMsg.postValue("Please enter your email and try again")
            return
        }
        authRepository.forgotPassword(email)
        toastMsg.postValue("Email Sent")
    }

    fun register(email: String?, pass: String?, confirm: String?) {
        if (email.isNullOrBlank() || pass.isNullOrBlank() || confirm.isNullOrBlank()) {
            toastMsg.postValue(errorMsg)
            return
        }
        if (!isEmailValid(email)) {
            toastMsg.postValue("Please enter a valid email.")
            return
        }

        if (pass != confirm) {
            toastMsg.postValue("Passwords do not match, Try again.")
        } else {
            authRepository.fireRegis(email, pass)
        }
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }

    fun getToastMsgLiveData():MutableLiveData<String>{
        return toastMsg
    }

    fun getLoggingInLiveData():MutableLiveData<Boolean>{
        return loggingInLiveData
    }

    fun logOut() {
        authRepository.logOut()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
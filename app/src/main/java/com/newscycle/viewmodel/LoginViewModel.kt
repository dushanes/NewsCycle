package com.newscycle.viewmodel

import android.app.Application
import android.util.Patterns
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
    private val email: MutableLiveData<String> = MutableLiveData()
    private val pass: MutableLiveData<String> = MutableLiveData()
    private val passConfirm: MutableLiveData<String> = MutableLiveData()
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

    fun login(email: String?, pass: String?): Boolean {
        loggingInLiveData.value = true

        if (email.isNullOrBlank() || pass.isNullOrBlank()  || !isEmailValid(email)) {
            toastMsg.postValue(errorMsg)
            loggingInLiveData.value = false
            return false
        }

        authRepository.onFireSignIn(email, pass)
        return true
    }

    fun toRegister(view: View) {
        (view.context as AppCompatActivity).findNavController(R.id.content_fragment_container)
            .navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun forgotPassword(email: String?): Boolean {
        if (email.isNullOrBlank() || !isEmailValid(email)) {
            toastMsg.postValue("Please enter your email and try again")
            return false
        }

        authRepository.forgotPassword(email)
        toastMsg.postValue("Email Sent")
        return true
    }

    fun register(email: String?, pass: String?, confirm: String?): Boolean {
        if (email.isNullOrBlank() || pass.isNullOrBlank() || confirm.isNullOrBlank() || pass.length < 8) {
            toastMsg.postValue(errorMsg)
            return false
        }
        if (!isEmailValid(email)) {
            toastMsg.postValue("Please enter a valid email.")
            return false
        }

        return if (pass != confirm) {
            toastMsg.postValue("Passwords do not match, Try again.")
            false
        } else {
            authRepository.fireRegis(email, pass)
            true
        }
    }

    fun getEmailLiveData(): MutableLiveData<String>{
        return email
    }

    fun getPasswordLiveData(): MutableLiveData<String>{
        return pass
    }

    fun getPasswordConfirmLiveData(): MutableLiveData<String>{
        return passConfirm
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

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
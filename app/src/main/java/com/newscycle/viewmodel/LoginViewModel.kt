package com.newscycle.viewmodel

import android.animation.ObjectAnimator
import android.app.Application
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseUser
import com.newscycle.R
import com.newscycle.repositories.AuthRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var login_anim: ObjectAnimator
    private val authRepository: AuthRepository = AuthRepository(application)
    var loggedInLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var loggingInLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var pass: MutableLiveData<String> = MutableLiveData()
    var passConfirm: MutableLiveData<String> = MutableLiveData()
    var userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    var toastMsg: MutableLiveData<String> = MutableLiveData()

    init {
        authRepository.getUserLiveData().observeForever {
            userLiveData.postValue(it)
        }
        authRepository.getLoggedOutLiveData().observeForever {
            loggedInLiveData.postValue(it)
        }
    }

    fun login(email: String?, pass: String?) {
        loggingInLiveData.value = true
        if (email.isNullOrBlank() || pass.isNullOrBlank()) {
            toastMsg.value = "Please enter your email and password"
            loggingInLiveData.value = (false)
            return
        }

        if (!isEmailValid((email))) {
            toastMsg.postValue("Please enter a valid email")
            loggingInLiveData.postValue(false)
            return
        }
        authRepository.onFireSignIn(email, pass)
        loggingInLiveData.postValue(false)
    }

    fun toRegister(view: View) {
        (view.context as AppCompatActivity).findNavController(R.id.content_fragment_container)
            .navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun forgotPassword(email: String?) {
        if (email.isNullOrBlank()) {
            toastMsg.postValue("Please enter your email and try again")
            return
        }
        if (!isEmailValid(email)) return
        authRepository.forgotPassword(email)
        toastMsg.postValue("Email Sent")
    }

    fun register(email: String?, pass: String?, confirm: String?) {
        if (email.isNullOrBlank() || pass.isNullOrBlank() || confirm.isNullOrBlank()) {
            toastMsg.postValue("Please enter a username and password.")
            return
        } else if (!isEmailValid(email)) {
            toastMsg.postValue("Please enter a valid email.")
            return
        }

        if (pass != confirm) {
            toastMsg.postValue("Passwords do not match, Try again.")
        } else {
            authRepository.fireRegis(email, pass)
        }
    }

    fun logOut() {
        loggedInLiveData.postValue(false)
        authRepository.logOut()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
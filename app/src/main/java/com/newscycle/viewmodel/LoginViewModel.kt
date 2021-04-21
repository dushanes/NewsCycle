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
import com.newscycle.util.AuthUtil

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

    fun login(emailInput: String?, passInput: String?): Boolean {
        loggingInLiveData.value = true
        val email: String = emailInput ?: ""
        val pass: String = passInput ?: ""

        if (!AuthUtil.checkLoginCredentials(email, pass)) {
            toastMsg.value = (errorMsg)
            loggingInLiveData.value = false
            return false
        }

        authRepository.onFireSignIn(email, pass)
        loggingInLiveData.value = false
        return true
    }

    fun toRegister(view: View) {
        (view.context as AppCompatActivity).findNavController(R.id.content_fragment_container)
            .navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun forgotPassword(emailInput: String?): Boolean {
        val email = emailInput?: ""
        if (email.isEmpty()) {
            toastMsg.postValue("Please enter your email and try again")
            return false
        }

        authRepository.forgotPassword(email)
        toastMsg.postValue("Email Sent")
        return true
    }

    fun register(emailInput: String?, passInput: String?, confirmInput: String?): Boolean {
        loggingInLiveData.value = true
        val email: String = emailInput ?: ""
        val pass: String = passInput ?: ""
        val confirm: String = confirmInput ?: ""

        if (!AuthUtil.checkRegistrationCredentials(email, pass, confirm)) {
            toastMsg.postValue(errorMsg)
            loggingInLiveData.value = false
            return false
        }

        return if (!AuthUtil.checkPasswordMatches(pass, confirm)) {
            toastMsg.postValue("Passwords do not match, Try again.")
            loggingInLiveData.value = false
            false
        } else {
            authRepository.fireRegis(email, pass)
            loggingInLiveData.value = false
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
}
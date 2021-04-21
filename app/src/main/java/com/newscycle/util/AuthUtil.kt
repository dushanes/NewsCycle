package com.newscycle.util

object AuthUtil {
    fun checkLoginCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            return false
        }
        return true
    }

    fun checkRegistrationCredentials(email: String, password: String, passwordConfirm: String): Boolean {
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()  || password.length < 8) {
            return false
        }
        return true
    }

    fun checkPasswordMatches(password: String, passwordConfirm: String): Boolean{
        return password == passwordConfirm
    }
}
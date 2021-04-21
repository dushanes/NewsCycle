package com.newscycle.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class AuthUtilTest{

    @Test
    fun `valid login, return true`(){
        val result = AuthUtil.checkLoginCredentials(
            "email@email.com", "12345678")

        assertThat(result).isTrue()
    }

    @Test
    fun `invalid login, empty email return false`(){
        val result = AuthUtil.checkLoginCredentials(
            "", "12345678")

        assertThat(result).isFalse()
    }

    @Test
    fun `invalid login, empty password return false`(){
        val result = AuthUtil.checkLoginCredentials(
            "email@email.com", "")

        assertThat(result).isFalse()
    }

    @Test
    fun `valid registration, return true`(){
        val result = AuthUtil.checkRegistrationCredentials(
            "email@email.com", "12345678", "12345678")

        assertThat(result).isTrue()
    }


    @Test
    fun `invalid registration, password too short return false`(){
        val result = AuthUtil.checkRegistrationCredentials(
            "email@email.com", "1234567", "1234567")

        assertThat(result).isFalse()
    }

    @Test
    fun `valid password match, return true`(){
        val result = AuthUtil.checkPasswordMatches(
            "12345678", "1234568")

        assertThat(result).isTrue()
    }

    @Test
    fun `invalid password match, password don't match return false`(){
        val result = AuthUtil.checkPasswordMatches(
            "12345679", "1234568")

        assertThat(result).isFalse()
    }
}
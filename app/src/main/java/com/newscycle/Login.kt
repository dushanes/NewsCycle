package com.newscycle

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.newscycle.databinding.ActivityLoginBinding
import com.newscycle.viewmodel.LoginViewModel

class Login : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel

        loginViewModel.getToastMsgLiveData().observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

    }

}
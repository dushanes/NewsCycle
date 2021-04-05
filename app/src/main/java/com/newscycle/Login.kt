package com.newscycle

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.newscycle.databinding.ActivityLoginBinding
import com.newscycle.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class Login : AppCompatActivity() {
    lateinit var loginViewModel: LoginViewModel
    lateinit var loginAnim: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel

        loginViewModel.toastMsg.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        loginViewModel.loggingInLiveData.observe(this, {
            if (it) {
                loginAnim = createLoginLoading(true)
                loginAnim.start()
            } else {
                //if(loginAnim == null) return@observe
                loginAnim.doOnEnd {
                    loginAnim = createLoginLoading(false)
                    loginAnim.start()
                }
            }
        })
    }

    private fun createLoginLoading(isEntering: Boolean): ObjectAnimator {
        val anim: ObjectAnimator = if (isEntering) ObjectAnimator.ofFloat(
            login_loading,
            "x",
            login_loading.x,
            login_loading.x + login_loading.width
        ) else ObjectAnimator.ofFloat(
            login_loading,
            "x",
            login_loading.x,
            login_loading.x - login_loading.width
        )
        if (isEntering) anim.interpolator = BounceInterpolator() else anim.startDelay = 100
        anim.duration = 1000
        return anim
    }
}
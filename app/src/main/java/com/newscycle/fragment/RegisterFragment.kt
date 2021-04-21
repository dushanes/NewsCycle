package com.newscycle.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseUser
import com.newscycle.R
import com.newscycle.databinding.FragmentRegisterBinding
import com.newscycle.viewmodel.LoginViewModel


class RegisterFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private var loginAnim: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val loginObserver = Observer<FirebaseUser> {
            if (it != null) {
                activity?.findNavController(R.id.content_fragment_container)
                    ?.navigate(R.id.action_registerFragment_to_home)
            }
        }
        loginViewModel.getUserLiveData().observe(this, loginObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel
        setupAnim(binding)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val loginObserver = Observer<FirebaseUser> {
            if (it != null) {
                requireActivity().findNavController(R.id.content_fragment_container)
                    .navigate(R.id.action_registerFragment_to_home)
            }
        }
        loginViewModel.getUserLiveData().observe(requireActivity(), loginObserver)
    }

    fun setupAnim(binding: FragmentRegisterBinding){
        loginViewModel.getLoggingInLiveData().observe(viewLifecycleOwner, {
            if (it) {
                loginAnim = createLoginLoading(true, binding)
                loginAnim?.start()
            } else {
                loginAnim?.doOnEnd {
                    loginAnim = createLoginLoading(false, binding)
                    loginAnim?.start()
                }
            }
        })
    }

    private fun createLoginLoading(isEntering: Boolean, binding: FragmentRegisterBinding): ObjectAnimator {
        val anim: ObjectAnimator = if (isEntering) ObjectAnimator.ofFloat(
            binding.loginLoading,
            "x",
            binding.loginLoading.x,
            binding.loginLoading.x + binding.loginLoading.width
        ) else ObjectAnimator.ofFloat(
            binding.loginLoading,
            "x",
            binding.loginLoading.x,
            binding.loginLoading.x - binding.loginLoading.width
        )
        if (isEntering) anim.interpolator = BounceInterpolator() else anim.startDelay = 100
        anim.duration = 1000
        return anim
    }
}
package com.newscycle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseUser
import com.newscycle.R
import com.newscycle.databinding.FragmentNewUserBinding
import com.newscycle.viewmodel.LoginViewModel


class RegisterFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNewUserBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_new_user, container, false)
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel
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
        loginViewModel.userLiveData.observe(requireActivity(), loginObserver)
    }

}
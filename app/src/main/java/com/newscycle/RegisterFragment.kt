package com.newscycle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment(){
    private val mAuth: FirebaseAuth= FirebaseAuth.getInstance()
    private val TAG: String = "Register-Fragment:"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_user, container, false)
    }

}
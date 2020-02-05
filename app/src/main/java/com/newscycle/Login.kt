package com.newscycle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*

class Login : AppCompatActivity(), View.OnClickListener{
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var root: LinearLayout
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        supportFragmentManager.beginTransaction().add(R.id.content_fragment_container,LoginFragment(), "login").commitNow()
    }

    override fun onResume() {
        super.onResume()
        setupViews()
    }

    private fun setupViews() {
        editTextUsername = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.button_login)
        buttonRegi = findViewById(R.id.button_register)
        buttonLogin.setOnClickListener(this)
        buttonRegi.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view == button_login){
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }else if(view == button_register){
            val registerFragment = RegisterFragment()
            val tx = supportFragmentManager.beginTransaction()
                .add(R.id.content_fragment_container, registerFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    fun updateUI(firebaseUser: FirebaseUser){
        TODO("Implement UI updates if user is already logged in")
    }


}
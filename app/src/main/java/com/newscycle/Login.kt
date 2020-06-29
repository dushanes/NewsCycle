package com.newscycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.newscycle.fragment.RegisterFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_new_user.*


class Login : AppCompatActivity(), View.OnClickListener{
    private lateinit var mAuth: FirebaseAuth
    private val TAG: String = "Login"
    private lateinit var context: Context
    private val registerFragment = RegisterFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        context = this

        supportFragmentManager.beginTransaction()
            .add(R.id.content_fragment_container,LoginFragment(), "LOGIN")
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        setupViews()
    }

    private fun setupViews() {
        button_login.setOnClickListener(this)
        button_register.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            button_login ->
            {
                fireSignIn(
                    email.text.toString(),
                    password.text.toString()
                )
            }
            button_register ->
            {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_fragment_container, registerFragment, "REG")
                    .addToBackStack(null)
                    .commit()
                supportFragmentManager.executePendingTransactions()
                button_confirm.setOnClickListener(this)
            }
            button_confirm ->
            {
                val email = regi_email.text.toString()
                val pass = regi_password.text.toString()
                val passConfirm = regi_password_confirm.text.toString()

                    if(pass != passConfirm){
                        Toast.makeText(context,"Passwords do not match, Try again.",Toast.LENGTH_SHORT)
                            .show()
                    }else{
                        fireRegis(email, pass)
                    }
            }
        }
    }

    private fun portal(firebaseUser: FirebaseUser?){
        if(firebaseUser != null){
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            this.overridePendingTransition(R.anim.slide_enter_right, R.anim.slide_exit_left)
        }
        return
    }

    private fun fireRegis(email: String, pass: String){
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    this.portal(user!!)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    this.portal(null)
                }
            }
    }

    private fun fireSignIn(email: String, pass: String){
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(context, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
                    this.portal(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.portal(null)
                }
            }
    }


}
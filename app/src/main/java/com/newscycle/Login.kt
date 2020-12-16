package com.newscycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.newscycle.fragment.RegisterFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.*


class Login : AppCompatActivity(), View.OnClickListener{
    private val TAG: String = "Login"
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val registerFragment = RegisterFragment()
    private val loginFragment = LoginFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = Firebase.database.reference

        supportFragmentManager.beginTransaction()
            .add(R.id.content_fragment_container,loginFragment, "LOGIN")
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
        forget_password.setOnClickListener(this)
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
                    .add(R.id.content_fragment_container, registerFragment, "REG").hide(loginFragment)
                    .addToBackStack("extra")
                    .commit()
                supportFragmentManager.executePendingTransactions()
            }
            forget_password -> {
                val email = email.text.toString()

                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                        }
                    }
            }
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 1){
            supportFragmentManager.beginTransaction().remove(registerFragment).show(loginFragment).commitNowAllowingStateLoss()
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

    private fun fireSignIn(email: String, pass: String){
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
                    if (user != null){
                        database.child("users").child(user.uid).child("email").setValue(user.email)
                    }
                    this.portal(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.portal(null)
                }
            }
    }
}
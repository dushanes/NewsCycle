package com.newscycle.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.newscycle.Home
import com.newscycle.R
import kotlinx.android.synthetic.main.fragment_new_user.*


class RegisterFragment : Fragment(), View.OnClickListener{
    private val TAG: String = "Register"
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_new_user, container, false)
        database = Firebase.database.reference
        view.findViewById<Button>(R.id.button_confirm).setOnClickListener(this)
        return view
    }


    override fun onClick(v: View?) {
        when(v){
            button_confirm -> {
                if(regi_email.text == null || regi_email.text.toString() == ""){
                    Toast.makeText(context,"Please enter a username and password.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }else if (!isEmailValid(regi_email.text.toString())){
                    Toast.makeText(context,"Please enter a valid email.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val email = regi_email.text.toString()
                val pass = regi_password.text.toString()
                val passConfirm = regi_password_confirm.text.toString()

                if(pass != passConfirm){
                    Toast.makeText(context,"Passwords do not match, Try again.", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    fireRegis(email, pass)
                }
            }
        }
    }

    private fun fireRegis(email: String, pass: String){
        if(pass.length < 8){
            Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            val user = mAuth.currentUser
            if (task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
                if (user != null){
                    database.child("users").child(user.uid).child("email").setValue(user.email)
                }

                if(user != null){
                    val intent = Intent(context, Home::class.java)
                    startActivity(intent)
                }
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()

                if(user != null){
                    val intent = Intent(context, Home::class.java)
                    startActivity(intent)
                }
            }
        } }


    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
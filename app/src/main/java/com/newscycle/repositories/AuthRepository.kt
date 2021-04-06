package com.newscycle.repositories

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AuthRepository(private var application: Application) {
    private val TAG: String = "Login Regis Repo"
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val loggingInLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        if (firebaseAuth.currentUser != null) {
            firebaseUser.postValue(firebaseAuth.currentUser)
        }
    }

    fun onFireSignIn(email: String, pass: String) {
        Log.d(TAG, "onFireSignIn: ")
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(
                application.mainExecutor
            ) { task ->
                val user = firebaseAuth.currentUser
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    firebaseUser.postValue(user)
                    loggingInLiveData.postValue(false)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        application.applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun fireRegis(email: String, pass: String) {
        if (pass.length < 8) {
            Toast.makeText(
                application.applicationContext,
                "Password must be at least 8 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            val user = firebaseAuth.currentUser
            if (task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
                if (user != null) {
                    firebaseUser.postValue(user)
                    database.child("users").child(user.uid).child("email").setValue(user.email)
                }
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    application.applicationContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun forgotPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return firebaseUser
    }

    fun getLoggingInLiveData(): MutableLiveData<Boolean> {
        return loggingInLiveData
    }

}

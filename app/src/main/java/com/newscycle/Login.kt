package com.newscycle

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val editTextUername: EditText = findViewById(R.id.email)
        val editTextPassword: EditText = findViewById(R.id.password)
        val buttonLogin: Button = findViewById(R.id.button_login)
    }


}
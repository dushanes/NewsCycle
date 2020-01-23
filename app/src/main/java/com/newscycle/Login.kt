package com.newscycle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupViews()
    }

    private fun setupViews() {
        val editTextUername: EditText = findViewById(R.id.email)
        val editTextPassword: EditText = findViewById(R.id.password)
        val buttonLogin: Button = findViewById(R.id.button_login)

        buttonLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        })
    }

    fun onClick(view: View): View.OnClickListener?{
        if (view == button_login){
            return View.OnClickListener {

            }
        }
        return null
    }


}
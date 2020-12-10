package com.newscycle

import android.app.Activity
import android.os.Bundle

class Account(): Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
    }
}
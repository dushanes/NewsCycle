package com.newscycle

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.newscycle.api.ApiUtilities
import com.newscycle.api.SourcesModel

class EditFeed: Activity() {
    private val categories = listOf("General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology")
    private var returnedSources: ArrayList<SourcesModel> = ArrayList()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val apiUtil: ApiUtilities by lazy { ApiUtilities }
    private var userSources = HashMap<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_feed)
        val user = mAuth.currentUser
        database = Firebase.database.getReference(mAuth.currentUser?.uid.toString())
        val dropDownAdapter = ArrayAdapter(this, R.layout.list_item, categories)


    }

    override fun onPause() {
        super.onPause()
    }
}
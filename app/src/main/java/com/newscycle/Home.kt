package com.newscycle

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*


class Home: AppCompatActivity() {
    private lateinit var recyclerViewAdapter: RecyclerView.Adapter<*>
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val data = Array(5) {Article("Title", "content", Date(0), Uri.parse("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"))}
        val viewManager = LinearLayoutManager(this)

        val adapter = MainRecyclerViewAdapter(data)
        recyclerView_Home.adapter = adapter
        recyclerView_Home.layoutManager = viewManager
    }
}
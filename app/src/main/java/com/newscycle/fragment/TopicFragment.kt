package com.newscycle.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.newscycle.Constants
import com.newscycle.MainRecyclerViewAdapter
import com.newscycle.R
import kotlinx.android.synthetic.main.fragment_recyclerview.*

class TopicFragment (val activityContext: Context, val topic: String): Fragment(){
    private lateinit var recyclerViewAdapter: MainRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val TAG = "Topic Fragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_recyclerview, container, false)
        setupRecyclerView()
        return v
    }


    private fun setupRecyclerView() {
        Log.d(TAG, "Setting up recycler view")
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewAdapter = MainRecyclerViewAdapter(activityContext, Constants.TOPIC_FEED, recycler_view, linearLayoutManager, topic)
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.adapter = recyclerViewAdapter
    }
}
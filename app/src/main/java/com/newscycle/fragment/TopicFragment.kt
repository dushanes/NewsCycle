package com.newscycle.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newscycle.Feed
import com.newscycle.R
import com.newscycle.adapters.MainRecyclerViewAdapter

class TopicFragment(
    topic: String,
    val mTopic: String
) : Fragment() {
    private lateinit var recyclerViewAdapter: MainRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val TAG = "Topic Fragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_recyclerview, container, false)
        setupRecyclerView(v)
        return v
    }

    private fun setupRecyclerView(v: View) {
        Log.d(TAG, "Setting up recycler view")
        linearLayoutManager = LinearLayoutManager(context)
        val recView = v.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerViewAdapter = MainRecyclerViewAdapter(
            Feed.TOPIC_FEED,
            recView,
            linearLayoutManager,
            mTopic
        )
        recView.layoutManager = linearLayoutManager
        recView.adapter = recyclerViewAdapter
    }
}
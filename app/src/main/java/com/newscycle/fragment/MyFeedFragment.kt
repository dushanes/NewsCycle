package com.newscycle.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newscycle.Constants
import com.newscycle.MainRecyclerViewAdapter
import com.newscycle.R


class MyFeedFragment (val activityContext: Context): Fragment(){
    private lateinit var recyclerViewAdapter: MainRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val tempQuery = "Politics"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_recyclerview, container, false)
        setupRecyclerView(v)
        return v
    }


    private fun setupRecyclerView(v : View) {
        Log.d(Constants.MY_FEED, "Setting up recycler view")
        val recView = v.findViewById<RecyclerView>(R.id.recycler_view)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewAdapter = MainRecyclerViewAdapter(activityContext, Constants.MY_FEED, recView, linearLayoutManager, tempQuery)

        Log.d(Constants.MY_FEED, "Adapter Instantiated")
        recView.layoutManager = linearLayoutManager
        recView.adapter = recyclerViewAdapter
    }
}
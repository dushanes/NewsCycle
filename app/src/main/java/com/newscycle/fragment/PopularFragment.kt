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


class PopularFragment (val activityContext: Context): Fragment(){
    private lateinit var recyclerViewAdapter: MainRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val TAG = "Popular Fragment"


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
        val recView = v.findViewById<RecyclerView>(R.id.recycler_view)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewAdapter = MainRecyclerViewAdapter(activityContext, Constants.POP_FEED, recView, linearLayoutManager,"")
        recView.layoutManager = linearLayoutManager
        recView.adapter = recyclerViewAdapter
    }
}
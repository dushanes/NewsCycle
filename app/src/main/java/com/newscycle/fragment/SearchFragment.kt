package com.newscycle.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newscycle.Constants
import com.newscycle.MainRecyclerViewAdapter
import com.newscycle.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment (val activityContext: Context): Fragment(){
    private lateinit var recyclerViewAdapter: MainRecyclerViewAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MainRecyclerViewAdapter
    private val TAG = "Search Fragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        setupRecyclerView(v)
        return v
    }

    override fun onResume() {
        super.onResume()
        initSearch()
    }

    fun initSearch(){
        search.queryHint = "Search"
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null ) return false
                adapter.getQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.clear()
                return false
            }
        })
    }

    private fun setupRecyclerView(v: View) {
        Log.d(TAG, "Setting up recycler view")

        val recView = v.findViewById<RecyclerView>(R.id.search_recyclerview)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewAdapter = MainRecyclerViewAdapter(activityContext, Constants.SEARCH_FEED, recView, linearLayoutManager, "")
        adapter = recyclerViewAdapter
        recView.layoutManager = linearLayoutManager
        recView.adapter = recyclerViewAdapter
    }
}
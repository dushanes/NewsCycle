package com.newscycle.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.newscycle.Constants
import com.newscycle.FragmentListener
import com.newscycle.R

class CategoriesFragment (val activityContext: Context): Fragment(){
    private val values = arrayOf("BUSINESS", "ENTERTAINMENT", "GENERAL", "HEALTH", "SCIENCE", "SPORTS", "TECHNOLOGY")
    private lateinit var gridViewAdapter: ArrayAdapter<String>
    lateinit var listener: FragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_categories, container, false)
        setupGridView(v)
        listener = activity as FragmentListener
        return v
    }

    private fun setupGridView(v: View) {
        Log.d(Constants.CAT_FEED, "Setting up recycler view")
        val gridView = v.findViewById<GridView>(R.id.category_grid)
        gridViewAdapter = ArrayAdapter(activityContext, R.layout.category_card, R.id.category_text, values)
        gridView.adapter = gridViewAdapter
        gridView.setOnItemClickListener {
                _: AdapterView<*>?,
                view: View?,
                _: Int,
                _: Long ->
            listener.sendQuery(view?.findViewById<TextView>(R.id.category_text)?.text.toString())
        }
    }
}


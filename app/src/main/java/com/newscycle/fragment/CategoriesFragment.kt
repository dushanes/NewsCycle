package com.newscycle.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.newscycle.Constants
import com.newscycle.FragmentListener
import com.newscycle.R
import kotlinx.android.synthetic.main.fragment_categories.*

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
        setupGridView()
        listener = activity as FragmentListener
        return v
    }

    private fun setupGridView() {
        Log.d(Constants.CAT_FEED, "Setting up recycler view")
        gridViewAdapter = ArrayAdapter(activityContext, R.layout.category_card, R.id.category_text, values)
        category_grid.adapter = gridViewAdapter
        category_grid.setOnItemClickListener {
                _: AdapterView<*>?,
                view: View?,
                _: Int,
                _: Long ->
            listener.sendQuery(view?.findViewById<TextView>(R.id.category_text)?.text.toString())
        }
    }
}


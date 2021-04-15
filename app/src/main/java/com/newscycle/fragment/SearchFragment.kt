package com.newscycle.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.newscycle.Feed
import com.newscycle.R
import com.newscycle.adapters.MainRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = listOf("Relevance", "Popularity", "Recent")
        val dateItems = listOf("Today", "Past Week", "Past Month")

        search.requestFocus()
        val sortByAdapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (sort_by_edit_text as AutoCompleteTextView).setAdapter(sortByAdapter)

        val dateAdapter = ArrayAdapter(requireContext(), R.layout.list_item, dateItems)
        (from_date_edit_text as AutoCompleteTextView).setAdapter(dateAdapter)

        from_date_edit_text.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>, _: View, _: Int, _: Long ->
                search.requestFocus()
            }
        sort_by_edit_text.onItemClickListener =
            AdapterView.OnItemClickListener { _: AdapterView<*>, _: View, _: Int, _: Long ->
                search.requestFocus()
            }
        initSearch()
    }

    override fun onResume() {
        super.onResume()
        search.requestFocus()

    }

    fun initSearch() {
        (search as TextInputEditText).setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = search.text.toString()
                var sortBy: String = sort_by_edit_text.text.toString()
                sortBy = when (sortBy) {
                    "Relevance" -> "relevancy"
                    "Popularity" -> "popularity"
                    "Recent" -> "publishedAt"
                    else -> "publishedAt"
                }
                val fromDate = from_date_edit_text.text.toString()
                adapter.getQuery(query, sortBy, fromDate)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun setupRecyclerView(v: View) {
        Log.d(TAG, "Setting up recycler view")

        val recView = v.findViewById<RecyclerView>(R.id.search_recyclerview)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerViewAdapter = MainRecyclerViewAdapter(
            Feed.SEARCH_FEED,
            recView,
            linearLayoutManager,
            ""
        )
        adapter = recyclerViewAdapter
        recView.layoutManager = linearLayoutManager
        recView.adapter = recyclerViewAdapter
    }
}
package com.newscycle.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.newscycle.Feed
import com.newscycle.R
import com.newscycle.adapters.ArticlesListPagingAdapter
import com.newscycle.databinding.FragmentSearchBinding
import com.newscycle.viewmodel.ArticleListViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SearchFragment(fm: FragmentManager) : Fragment() {
    private val articleListViewModel: ArticleListViewModel = ArticleListViewModel(Feed.SEARCH_FEED)
    private val articlesListPagingAdapter: ArticlesListPagingAdapter = ArticlesListPagingAdapter(fm)
    private val linearLayoutManager: LinearLayoutManager  = LinearLayoutManager(context)
    private val TAG = "Search Fragment"
    private var binding: FragmentSearchBinding? = null

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_search, container, false)
        this.binding = binding
        binding.searchContainer.setEndIconOnClickListener {
            val query: String  = binding?.search?.text.toString()
            var sortBy: String = binding.sortByEditText.text.toString()
            val fromDate: String = binding?.fromDateEditText?.text.toString()
            println("Sort by what" + sortBy)
            sortBy = when (sortBy) {
                "Relevance" -> "relevancy"
                "Popularity" -> "popularity"
                "Recent" -> "publishedAt"
                else -> "relevancy"
            }

            println("Sort by what" + sortBy)
            articleListViewModel.getArticles(query,fromDate, sortBy).subscribe{
                articlesListPagingAdapter.submitData(lifecycle, it)
            }
        }
        setupRecyclerView(binding)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = listOf("Relevance", "Popularity", "Recent")
        val dateItems = listOf("Today", "Past Week", "Past Month")

        search.requestFocus()
        val sortByAdapter = ArrayAdapter(view.context, R.layout.list_item, items)
        binding?.sortByEditText?.setAdapter(sortByAdapter)

        val dateAdapter = ArrayAdapter(view.context, R.layout.list_item, dateItems)
        binding?.fromDateEditText?.setAdapter(dateAdapter)

        binding?.fromDateEditText?.onItemClickListener = returnToSearchListener
        binding?.sortByEditText?.onItemClickListener = returnToSearchListener
        initSearch()
    }

    override fun onResume() {
        super.onResume()
        search.requestFocus()

    }

    @ExperimentalCoroutinesApi
    fun initSearch() {
        binding?.search?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query: String  = binding?.search?.text.toString()
                var sortBy: String = binding?.sortByEditText?.text.toString()
                val fromDate: String = binding?.fromDateEditText?.text.toString()

                sortBy = when (sortBy) {
                    "Relevance" -> "relevancy"
                    "Popularity" -> "popularity"
                    "Recent" -> "publishedAt"
                    else -> "relevancy"
                }

                articleListViewModel.getArticles(query,fromDate, sortBy).subscribe{
                    articlesListPagingAdapter.submitData(lifecycle, it)
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun setupRecyclerView(binding: FragmentSearchBinding) {
        Log.i(TAG, "Setting up recycler view")
        binding.searchRecyclerview.layoutManager = linearLayoutManager
        binding.searchRecyclerview.adapter = articlesListPagingAdapter
    }

    private val returnToSearchListener  =
        AdapterView.OnItemClickListener { _, _, _, _ ->
            binding?.search?.requestFocus()
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
}
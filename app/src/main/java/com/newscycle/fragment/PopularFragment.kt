package com.newscycle.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.newscycle.Feed
import com.newscycle.R
import com.newscycle.adapters.ArticlesListPagingAdapter
import com.newscycle.databinding.FragmentRecyclerviewBinding
import com.newscycle.viewmodel.ArticleListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


class PopularFragment(fm: FragmentManager) : Fragment() {
    private val articleListViewModel: ArticleListViewModel = ArticleListViewModel(Feed.POP_FEED)
    private val articlesListPagingAdapter: ArticlesListPagingAdapter = ArticlesListPagingAdapter(fm)
    private val linearLayoutManager: LinearLayoutManager  = LinearLayoutManager(context)
    private val TAG = "Popular Fragment"


    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRecyclerviewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_recyclerview, container, false)
        setupRecyclerView(binding)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    private fun setupRecyclerView(binding: FragmentRecyclerviewBinding) {
        Log.d(TAG, "Setting up recycler view")
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = articlesListPagingAdapter
        articleListViewModel.getArticles("").subscribe{
            articlesListPagingAdapter.submitData(lifecycle, it)
        }
    }
}
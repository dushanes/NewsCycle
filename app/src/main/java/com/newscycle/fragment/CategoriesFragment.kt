package com.newscycle.fragment

//import com.newscycle.FragmentListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.newscycle.Constants
import com.newscycle.Feed
import com.newscycle.R
import com.newscycle.adapters.ArticlesListPagingAdapter
import com.newscycle.adapters.CategoryGridAdapter
import com.newscycle.databinding.FragmentCategoriesBinding
import com.newscycle.viewmodel.ArticleListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CategoriesFragment(fm: FragmentManager) : Fragment() {
    private val articleListViewModel: ArticleListViewModel = ArticleListViewModel(Feed.TOPIC_FEED)
    private val articlesListPagingAdapter: ArticlesListPagingAdapter = ArticlesListPagingAdapter(fm)
    private val linearLayoutManager: LinearLayoutManager  = LinearLayoutManager(context)


    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentCategoriesBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_categories, container, false)
        setupGridView(binding)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    private fun setupGridView(binding: FragmentCategoriesBinding) {
        Log.d("Category Menu", "Setting up recycler view")

        binding.categoryGrid.adapter = context?.let {
            CategoryGridAdapter(
                it,
                R.layout.category_card,
                R.id.category_text,
                Constants.values
            )
        }

        binding.categoryGrid.setOnItemClickListener { _: AdapterView<*>?,
                                               view: View?,
                                               pos: Int,
                                               id: Long ->
            Log.d("Grid item", "Button has been clicked")
            binding.categoryGrid.visibility = View.GONE
            binding.categoryRecyclerView.layoutManager = linearLayoutManager
            binding.categoryRecyclerView.adapter = articlesListPagingAdapter
            binding.categoryRecyclerView.visibility = View.VISIBLE
            articleListViewModel.getArticles(Constants.values[pos]).subscribe{
                articlesListPagingAdapter.submitData(lifecycle, it)
            }
        }
    }

}


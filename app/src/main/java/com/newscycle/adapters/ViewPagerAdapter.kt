package com.newscycle.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.newscycle.fragment.CategoriesFragment
import com.newscycle.fragment.PopularFragment
import com.newscycle.fragment.SearchFragment

class ViewPagerAdapter(val fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PopularFragment(fm)
            1 -> CategoriesFragment(fm)
            2 -> SearchFragment(fm)
            else -> PopularFragment(fm)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}
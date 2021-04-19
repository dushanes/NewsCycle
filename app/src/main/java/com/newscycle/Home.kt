package com.newscycle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.newscycle.databinding.ActivityHomeBinding
import com.newscycle.fragment.CategoriesFragment
import com.newscycle.fragment.PopularFragment
import com.newscycle.fragment.SearchFragment
import com.newscycle.util.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_categories.*

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val NUM_PAGES = 3
    private val tabTitles = listOf("HEADLINES", "TOPICS", "SEARCH")
    private val icons = listOf(R.drawable.popular, R.drawable.category, R.drawable.search)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_home)
        initTabLayout()
        inflateDrawer()
    }

    private fun initTabLayout() {
        tab_layout.tabGravity = TabLayout.GRAVITY_FILL
        pager.setPageTransformer(ZoomOutPageTransformer())
        pager.adapter = ViewPagerAdapter(supportFragmentManager)

        //connect tab_layout to pager
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = tabTitles[position]
            tab.setIcon(icons[position])
        }.attach()

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                return
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { pager.setCurrentItem(it, true) }
            }
        })
    }

    private fun inflateDrawer() {
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(R.drawable.menu)
            setDisplayShowTitleEnabled(false)
        }
        toolbar.z = 1.05F

        findViewById<NavigationView>(R.id.nav).setNavigationItemSelectedListener(this)
    }

    //------------------------------Navigation functions-----------------------------------------------//
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.title == "ACCOUNT") {
            return true
        } else if (menuItem.title == "EDIT FEED") {
            return true
        }
        return true
    }

    override fun onBackPressed() {
        if (!home_drawer.isDrawerOpen(GravityCompat.START)) {
            if (category_recycler_view.visibility == View.VISIBLE) {
                category_recycler_view.adapter = null
                category_recycler_view.visibility = View.GONE
                category_grid.visibility = View.VISIBLE
                return
            }else{
                val dialog = LogoutDialog()
                dialog.show(supportFragmentManager, "logout")
                return
            }
        }else if (home_drawer.isDrawerOpen(GravityCompat.START)) {
            home_drawer.closeDrawer(GravityCompat.START)
            return
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                Log.d("Nav", "Menu button clicked")
                home_drawer.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    //------------------------Inner Classes------------------------------------------------------//


    inner class ViewPagerAdapter(val fm: FragmentManager) : FragmentStateAdapter(fm, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> PopularFragment(fm)
                1 -> CategoriesFragment(fm)
                2 -> SearchFragment(fm)
                else -> PopularFragment(fm)
            }
        }

        override fun getItemCount(): Int {
            return NUM_PAGES
        }
    }
}



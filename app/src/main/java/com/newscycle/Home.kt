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
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.newscycle.adapters.ViewPagerAdapter
import com.newscycle.databinding.ActivityHomeBinding
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
        initTabLayout(binding)
        inflateDrawer()
    }

    private fun initTabLayout(binding: ActivityHomeBinding) {
        val pager = binding.pager
        val tabLayout = binding.tabLayout

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        pager.setPageTransformer(ZoomOutPageTransformer())
        pager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        //connect tab_layout to pager
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = tabTitles[position]
            tab.setIcon(icons[position])
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val imm =
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
            if (category_recycler_view != null && category_recycler_view.visibility == View.VISIBLE) {
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

}
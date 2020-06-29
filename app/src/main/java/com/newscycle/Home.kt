package com.newscycle

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.newscycle.fragment.*
import kotlinx.android.synthetic.main.activity_home.*

interface TopicSelect {
    fun onTopicSelect(topic: String)
}

class Home: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val NUM_PAGES = 4
    private val tabTitles = listOf("MY FEED", "POPULAR", "TOPICS", "SEARCH")
    private val icons = listOf(R.drawable.my_feed, R.drawable.popular, R.drawable.category, R.drawable.search)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fm = supportFragmentManager
        pager.adapter = ViewPagerAdapter(fm)
        appName.setOnClickListener { pager.setCurrentItem(0, true) }
        initTabLayout()
        inflateDrawer()
    }

    private fun initTabLayout(){
        for (i in 0..3){
            tab_layout.addTab(tab_layout.newTab()
                .setText(tabTitles[i])
                .setIcon(icons[i]))
        }
        tab_layout.tabGravity = TabLayout.GRAVITY_FILL
        pager.setOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                return
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { pager.setCurrentItem(it, true) }
            }
        })

    }

    private fun inflateDrawer(){
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && !home_drawer.isDrawerOpen(GravityCompat.START)) {
            val dialog = LogoutDialog()
            dialog.show(supportFragmentManager, "logout")
            true
        } else super.onKeyDown(keyCode, event)
    }

//------------------------------Navigation functions-----------------------------------------------//
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        TODO("Implement navigation upon item selection")
        return true
    }

    override fun onBackPressed() {
        if(home_drawer.isDrawerOpen(GravityCompat.START)){
            home_drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
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


    inner class ViewPagerAdapter constructor (fragmentManager: FragmentManager):  FragmentStatePagerAdapter(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
        inner class TopicSelectListener: TopicSelect {
            override fun onTopicSelect(topic: String) {
                fm.beginTransaction().remove(tempFragment).commit()

                if(tempFragment is CategoriesFragment){
                    tempFragment = TopicFragment(this@Home, topic, listener)
                }else{
                    tempFragment = CategoriesFragment(this@Home)
                }
                notifyDataSetChanged()
            }
        }

        val fm = fragmentManager
        lateinit var tempFragment : Fragment
        val listener = TopicSelectListener()

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> MyFeedFragment(this@Home)
                1 -> PopularFragment(this@Home)
                2 -> {
                    tempFragment = CategoriesFragment(this@Home)
                    return tempFragment
                }
                3 -> SearchFragment(this@Home)
                else -> MyFeedFragment(this@Home)
            }
        }

        override fun getCount(): Int {
            return NUM_PAGES
        }
    }
}



package com.newscycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.newscycle.fragment.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.fragment_categories.*

class Home: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val NUM_PAGES = 4
    private val tabTitles = listOf("MY FEED", "HEADLINES", "TOPICS", "SEARCH")
    private val icons = listOf(R.drawable.my_feed, R.drawable.popular, R.drawable.category, R.drawable.search)
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        appNameHome.isClickable = true
        (appNameHome as TextView).setOnClickListener {
            pager.setCurrentItem(0, true)
            Toast.makeText(this, "button has been clicked", Toast.LENGTH_LONG).show()
        }
        initTabLayout()
        inflateDrawer()
    }

    private fun initTabLayout(){
        tab_layout.tabGravity = TabLayout.GRAVITY_FILL
        pager.adapter = ViewPagerAdapter(supportFragmentManager)

        //connect tab_layout to pager
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = tabTitles[position]
            tab.setIcon(icons[position])
        }.attach()

        tab_layout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
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

    override fun onResume() {
        super.onResume()
        val user = mAuth.currentUser
        drawer_email?.text = user?.email
        drawer_user_name?.text = user?.displayName
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && !home_drawer.isDrawerOpen(GravityCompat.START)) {
            if (pager.currentItem == 2){
                if (category_grid.visibility == View.GONE){
                    categ_recycler_view.adapter = null
                    categ_recycler_view.visibility = View.GONE
                    category_grid.visibility = View.VISIBLE
                    return true
                }
            }
            val dialog = LogoutDialog()
            dialog.show(supportFragmentManager, "logout")
            true
        } else super.onKeyDown(keyCode, event)
    }

    //------------------------------Navigation functions-----------------------------------------------//
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.title == "ACCOUNT"){
            val intent = Intent(this, Account::class.java)
            startActivity(intent)
            return true
        }else if (menuItem.title == "EDIT FEED"){
            val intent = Intent(this, EditFeed::class.java)
            startActivity(intent)
            return true
        }
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


    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentStateAdapter(fm, lifecycle){
        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> MyFeedFragment(this@Home)
                1 -> PopularFragment(this@Home)
                2 -> CategoriesFragment(this@Home)
                3 -> SearchFragment(this@Home)
                else -> MyFeedFragment(this@Home)
            }
        }

        override fun getItemCount(): Int {
            return NUM_PAGES
        }
    }
}



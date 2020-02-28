package com.newscycle

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import com.google.android.material.navigation.NavigationView
import com.newscycle.fragment.CategoriesFragment
import com.newscycle.fragment.MyFeedFragment
import com.newscycle.fragment.PopularFragment
import com.newscycle.fragment.TopicFragment
import kotlinx.android.synthetic.main.activity_home.*

interface FragmentListener{
    fun sendQuery(query: String)
}

class Home: AppCompatActivity(), View.OnTouchListener, NavigationView.OnNavigationItemSelectedListener, FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        categories.setOnTouchListener(this)
        popular.setOnTouchListener(this)
        my_feed_button.setOnTouchListener(this)
        my_feed_button.isPressed = true

        supportFragmentManager.beginTransaction()
            .add(R.id.home_content_fragment_container, MyFeedFragment(this), "MYF")
            .addToBackStack(null)
            .commit()
        inflateDrawer()
    }

    private fun inflateDrawer(){
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(R.drawable.menu)
        }

        findViewById<NavigationView>(R.id.nav).setNavigationItemSelectedListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean{
        when (v) {
            my_feed_button -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    Log.d(Constants.HOME, "My Feed Button Touched!")
                    v.isPressed = true
                    ViewCompat.setElevation(v, 2f)
                    popular.isPressed = false
                    categories.isPressed = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content_fragment_container, MyFeedFragment(this))
                        .addToBackStack(null)
                        .commit()
                }
                return true
            }
            popular -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    Log.d(Constants.HOME, "Popular Button Clicked!")
                    v.isPressed = true
                    my_feed_button.isPressed = false
                    categories.isPressed = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content_fragment_container, PopularFragment(this))
                        .addToBackStack(null)
                        .commit()
                }
                return true
            }
            categories -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    Log.d(Constants.HOME, "Category Button Clicked!")
                    v.isPressed = true
                    my_feed_button.isPressed = false
                    popular.isPressed = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content_fragment_container, CategoriesFragment(this))
                        .addToBackStack(null)
                        .commit()
                }
                return true
            }
            else -> return false
        }
    }

    override fun sendQuery(query: String) {
        if (!isFinishing && !isDestroyed) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_content_fragment_container, TopicFragment(this, query))
                .addToBackStack(null)
                .commit()
        }
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
}
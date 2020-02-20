package com.newscycle

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.newscycle.fragment.CategoriesFragment
import com.newscycle.fragment.MyFeedFragment
import com.newscycle.fragment.PopularFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_sidebar.*


class Home: FragmentActivity(), View.OnClickListener, View.OnTouchListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var nav: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val TAG = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        categories.setOnTouchListener(this)
        popular.setOnTouchListener(this)
        my_feed_button.setOnTouchListener(this)
        user_name.text = firebaseUser!!.displayName
        my_feed_button.isPressed = true

        supportFragmentManager.beginTransaction()
            .add(R.id.home_content_fragment_container, MyFeedFragment(this), "MYF")
            .addToBackStack(null)
            .commit()
        inflateDrawer()
    }

    private fun inflateDrawer(){
        val v = layoutInflater.inflate(R.layout.home_sidebar, root_home, false)
        drawer = v.findViewById(R.id.home_drawer)
        nav = v.findViewById(R.id.nav)

        toggle = ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        drawer.addDrawerListener(toggle)

        toggle.syncState()
        nav.setNavigationItemSelectedListener(this)
        drawer_button.setOnClickListener(this)
    }

    override fun onClick(v: View){
        when (v) {
            drawer_button -> {
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START)
                }else {
                    drawer.openDrawer(GravityCompat.START)
                }
            }
        }
    }

    override fun onTouch(v: View, event: MotionEvent):Boolean{
        when (v) {
            my_feed_button -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "My Feed Button Touched!")
                    v.isPressed = true
                    ViewCompat.setElevation(v, 2f)
                    popular.isPressed = false
                    categories.isPressed = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content_fragment_container, MyFeedFragment(this))
                        .commit()
                }
                return true
            }
            popular -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "Popular Button Clicked!")
                    v.isPressed = true
                    my_feed_button.isPressed = false
                    categories.isPressed = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content_fragment_container, PopularFragment(this))
                        .commit()
                }
                return true
            }
            categories -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "Category Button Clicked!")
                    v.isPressed = true
                    my_feed_button.isPressed = false
                    popular.isPressed = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content_fragment_container, CategoriesFragment(this))
                        .commit()
                }
                return true
            }
            else -> return false
        }
    }

//------------------------------Navigation functions-----------------------------------------------//
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        //drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(home_drawer.isDrawerOpen(GravityCompat.START)){
            home_drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
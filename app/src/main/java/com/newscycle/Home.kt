package com.newscycle

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_sidebar.*


class Home: Activity(), View.OnClickListener, View.OnTouchListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerViewAdapter: RecyclerView.Adapter<*>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var nav: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        categories.setOnTouchListener(this)
        popular.setOnTouchListener(this)
        myFeed.setOnTouchListener(this)
        setupRecyclerView()
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

    private fun setupRecyclerView(){
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = MainRecyclerViewAdapter(this)
        recyclerView_Home.adapter = recyclerViewAdapter
        recyclerView_Home.layoutManager = linearLayoutManager
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
            myFeed -> {
            }
            categories -> {
            }
            popular -> {
            }
        }
    }

    override fun onTouch(v: View, event: MotionEvent):Boolean{
        when (v) {
            myFeed -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    v.isPressed = !v.isPressed
                    ViewCompat.setElevation(v, 2f)
                    popular.isPressed = false
                    categories.isPressed = false
                    v.performClick()
                }
                return true
            }
            popular -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    v.isPressed = !v.isPressed
                    myFeed.isPressed = false
                    categories.isPressed = false
                    v.performClick()
                }
                return true
            }
            categories -> {
                val action: Int = event.action

                if (action == MotionEvent.ACTION_DOWN) {
                    v.isPressed = true
                    myFeed.isPressed = false
                    popular.isPressed = false
                    v.performClick()
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
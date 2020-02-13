package com.newscycle

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.newscycle.api.Results
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_sidebar.*


class Home: Activity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerViewAdapter: RecyclerView.Adapter<*>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var nav: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var results: Single<Results>
    private lateinit var data: Array<Results>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
        if(v == drawer_button){
            if(drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START)
            }else {
                drawer.openDrawer(GravityCompat.START)
            }
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
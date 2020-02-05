package com.newscycle

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_sidebar.*
import java.util.*


class Home: AppCompatActivity(), View.OnClickListener {
    private lateinit var recyclerViewAdapter: RecyclerView.Adapter<*>
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        drawer_button.setOnClickListener(this)

        //setupRecyclerView() TODO(Uncomment after setting up REST API pipeline)
        inflateDrawer()
    }

    private fun inflateDrawer(){
        val v = layoutInflater.inflate(R.layout.home_sidebar, root_home, true)
        drawer = v.findViewById(R.id.home_drawer)
        toolbar = v.findViewById(R.id.toolbar)

        toggle = ActionBarDrawerToggle(this, home_drawer, toolbar, R.string.drawer_open, R.string.drawer_close)
        home_drawer.addDrawerListener(toggle)
        setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView(){
        val data = Array(5) {ArticleViewModel("Title", "content", Date(0), Uri.parse("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"))}
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = MainRecyclerViewAdapter(data)
        recyclerView_Home.adapter = recyclerViewAdapter
        recyclerView_Home.layoutManager = linearLayoutManager
    }

    override fun onClick(v: View){
        if(v == drawer_button){
            if(drawer.isDrawerOpen(Gravity.LEFT))
                drawer.closeDrawer(Gravity.LEFT)
            else
                drawer.openDrawer(Gravity.LEFT)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
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
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
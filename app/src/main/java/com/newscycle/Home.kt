package com.newscycle

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.ActionBar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawer_button.setOnClickListener(this)

        setupRecyclerView()
        inflateDrawer()
    }

    private fun inflateDrawer(){
        val v = layoutInflater.inflate(R.layout.home_sidebar, root_home, true)
        drawer = v.findViewById(R.id.home_drawer)

        toggle = ActionBarDrawerToggle(this, home_drawer, R.string.drawer_open, R.string.drawer_close)
        home_drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
    }

    private fun setupRecyclerView(){
        val data = Array(5) {Article("Title", "content", Date(0), Uri.parse("https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png"))}
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
}
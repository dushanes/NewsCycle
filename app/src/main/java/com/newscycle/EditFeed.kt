package com.newscycle

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.newscycle.api.ApiUtilities
import com.newscycle.api.SourcesModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_feed.*

class EditFeed: Activity() {
    private val categories = listOf("General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology")
    private var returnedSources: ArrayList<SourcesModel> = ArrayList()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val apiUtil: ApiUtilities by lazy { ApiUtilities }
    private var userSources = HashMap<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_feed)
        val user = mAuth.currentUser
        database = Firebase.database.getReference(mAuth.currentUser?.uid.toString())

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Fire Child Event Listener", "onChildAdded:" + dataSnapshot.key!!)

                userSources[dataSnapshot.key!!] = dataSnapshot.value as String
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Fire Child Event Listener", "onChildChanged: ${dataSnapshot.key}")

                userSources[dataSnapshot.key!!] = dataSnapshot.value as String
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Fire Child Event Listener", "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        database.child("sources").addChildEventListener(childEventListener)

        val dropDownAdapter = ArrayAdapter(this, R.layout.list_item, categories)
        val dropDownResultsAdapter = object : ArrayAdapter<SourcesModel>(this, R.layout.list_item_checkbox, returnedSources){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: View
                if (null == convertView){
                    view = layoutInflater.inflate(R.layout.list_item_checkbox,parent, false)
                }else{
                    view = convertView
                }
                view.findViewById<TextView>(R.id.list_item_text).text = getItem(position)?.name
                val cb = view.findViewById<CheckBox>(R.id.checkbox)
                var curItemID : String

                cb.setOnCheckedChangeListener { button, b ->  }
                for (item in userSources){
                    curItemID = getItem(position)?.id.toString()
                    if (item.value == curItemID){
                        cb.isChecked = true
                    }
                }
                return view
            }
        }

        edit_feed_search_select.setAdapter(dropDownAdapter)
        edit_feed_search_list.adapter = dropDownResultsAdapter
        edit_feed_search_list.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos, _ ->
            val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
            val selected = dropDownResultsAdapter.getItem(pos)?.id

            if (selected != null) {
                database.child("sources").child(selected)
            }
            if(!checkbox.isChecked){
                if (selected != null) {

                } else{
                    Log.d("Edit Feed Activity", "At pos ${pos} the id of the source is null")
                }
                checkbox.isChecked = true
            }else{
                userSources.remove(selected)
                checkbox.isChecked = false
            }
        }

        edit_feed_search_select.setOnItemClickListener { _, _, pos, _ ->
            returnedSources.clear()
            dropDownResultsAdapter.notifyDataSetChanged()
            apiUtil.getSources(category = categories[pos].toLowerCase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    returnedSources.addAll(t.sources)
                    dropDownResultsAdapter.notifyDataSetChanged()
                }, {error ->
                    error.message?.let { Log.d("Api Error:", it) }
                })
        }

    }

    override fun onPause() {
        super.onPause()
        database.child("users").child(mAuth.currentUser?.uid.toString()).child("sources").setValue(userSources)
    }
}
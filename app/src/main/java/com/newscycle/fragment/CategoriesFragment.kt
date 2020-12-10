package com.newscycle.fragment

//import com.newscycle.FragmentListener
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newscycle.Constants
import com.newscycle.MainRecyclerViewAdapter
import com.newscycle.R
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment(val activityContext: Context): Fragment(){
    private val values = arrayOf(
        "BUSINESS",
        "ENTERTAINMENT",
        "GENERAL",
        "HEALTH",
        "SCIENCE",
        "SPORTS",
        "TECHNOLOGY"
    )
    private val gridViewAdapter: ArrayAdapter<String> = ArrayAdapter(
        activityContext,
        R.layout.category_card,
        R.id.category_text,
        values
    )
    private val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private lateinit var recyclerViewAdapter: MainRecyclerViewAdapter
    private val cropOptions: RequestOptions = RequestOptions.centerCropTransform()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_categories, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGridView()
    }

    private fun setupGridView() {
        Log.d(Constants.CAT_FEED, "Setting up recycler view")

        category_grid.adapter = object :
            ArrayAdapter<String?>(activityContext, R.layout.category_card, R.id.category_text, values) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                var background = resources.getDrawable(R.drawable.clip_img_business)
                Log.d("GridView", "View Created with background: $background")
                when (position){
                    0 -> background = resources.getDrawable(R.drawable.clip_img_business, null)
                    1 -> background = resources.getDrawable(R.drawable.clip_img_entertain, null)
                    2 -> background = resources.getDrawable(R.drawable.clip_img_general, null)
                    3 -> background = resources.getDrawable(R.drawable.clip_img_health, null)
                    4 -> background = resources.getDrawable(R.drawable.clip_img_science, null)
                    5 -> background = resources.getDrawable(R.drawable.clip_img_sports, null)
                    6 -> background = resources.getDrawable(R.drawable.clip_img_tech, null)
                }
                Glide.with(activityContext)
                    .load(background)
                    .apply(cropOptions)
                    .into(view.findViewById(R.id.categ_card_img))
                //Log.d("Radius", "" + view.findViewById<CardView>(R.id.categ_card).radius)

                return view
            }
        }

        category_grid.setOnItemClickListener { _: AdapterView<*>?,
                                               view: View?,
                                               pos: Int,
                                               id: Long ->
            Log.d("Grid item", "Button has been clicked")
            category_grid.visibility = View.GONE
            recyclerViewAdapter = MainRecyclerViewAdapter(
                activityContext,
                Constants.TOPIC_FEED,
                categ_recycler_view,
                linearLayoutManager,
                values[pos]
            )
            categ_recycler_view.layoutManager = linearLayoutManager
            categ_recycler_view.adapter = recyclerViewAdapter
            categ_recycler_view.visibility = View.VISIBLE
            //adapterInterface.getQuery(view?.findViewById<TextView>(R.id.category_text)?.text.toString())
        }
    }
}


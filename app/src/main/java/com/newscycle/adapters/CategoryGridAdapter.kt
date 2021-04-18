package com.newscycle.adapters

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newscycle.R

class CategoryGridAdapter(context: Context,
                          @LayoutRes resource:Int,
                          @IdRes textViewResourceId: Int,
                          objects: Array<String>
): ArrayAdapter<String>(context, resource, textViewResourceId, objects) {
    private val cropOptions: RequestOptions = RequestOptions.centerCropTransform()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = super.getView(position, convertView, parent)
        var background = R.drawable.clip_img_business

        Log.d("GridView", "View Created with background: $background")

        when (position) {
            0 -> background = R.drawable.clip_img_business
            1 -> background = R.drawable.clip_img_entertain
            2 -> background = R.drawable.clip_img_general
            3 -> background = R.drawable.clip_img_health
            4 -> background = R.drawable.clip_img_science
            5 -> background = R.drawable.clip_img_sports
            6 -> background = R.drawable.clip_img_tech
        }
        Glide.with(context)
            .load(background)
            .apply(cropOptions)
            .into(view.findViewById(R.id.categ_card_img))

        return view
    }
}
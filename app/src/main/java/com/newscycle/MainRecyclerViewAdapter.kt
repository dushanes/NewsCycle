package com.newscycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainRecyclerViewAdapter(private val data: Array<ArticleViewModel>) : RecyclerView.Adapter<MainRecyclerViewAdapter.viewHolder>() {

    class viewHolder (val view: View) : RecyclerView.ViewHolder(view){
        var viewImage: ImageView
        var viewTitle: TextView
        var viewDescription: TextView
        var viewDate: TextView
        var viewSource: TextView

        init{
            viewImage = view.findViewById(R.id.card_image)
            viewTitle = view.findViewById(R.id.title)
            viewDescription = view.findViewById(R.id.description)
            viewDate = view.findViewById(R.id.time)
            viewSource = view.findViewById(R.id.source)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.article_card, parent, false)
        return viewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val cur = data[position]
        holder.viewSource.setText("source")
        holder.viewDate.setText(cur.getDate())
        holder.viewDescription.setText(cur.getContent())
        holder.viewTitle.setText(cur.getTitle())
        holder.viewImage.setImageURI(cur.getImageSRC())//To change body of created functions use File | Settings | File Templates.
    }
}
package com.example.garudabirdapp

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(var birdArrayList: ArrayList<Bird>, var context: Activity) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private lateinit var myListener: onItemClickListener
    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        myListener = listener
    }

    // to create new view instance
    // when layout manager fails to find a suitable view for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.each_row, parent, false)
        return MyViewHolder(itemView, myListener)
    }

    // populate items with data
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = birdArrayList[position]
        holder.hTitle.text = currentItem.birdName
        holder.hImage.setImageResource(currentItem.birdImage)
    }

    // how many list items are present in your array
    override fun getItemCount(): Int {
        return birdArrayList.size
    }

    // it holds the view so views are not created everytime, so memory can be saved
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {

        var hTitle: TextView = itemView.findViewById(R.id.headingTitle)
        var hImage: ShapeableImageView = itemView.findViewById(R.id.headingImage)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}
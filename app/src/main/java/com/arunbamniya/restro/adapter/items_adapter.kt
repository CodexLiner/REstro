package com.arunbamniya.restro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R

class items_adapter : RecyclerView.Adapter<items_adapter.holder>() {

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val view : View =  LayoutInflater.from(parent.context).inflate(R.layout.items_layout_single , parent , false)
        return holder(view)
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
//        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return 10
    }
}
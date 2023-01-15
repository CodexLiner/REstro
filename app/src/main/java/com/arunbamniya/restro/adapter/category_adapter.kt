package com.arunbamniya.restro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R
import com.arunbamniya.restro.interfaces.AdapterClicker
import com.arunbamniya.restro.network.CategoryResponse
import com.bumptech.glide.Glide

class category_adapter(private val list: List<CategoryResponse>? , val listner : AdapterClicker) :
    RecyclerView.Adapter<category_adapter.holder>() {
    class holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val category_name = itemView.findViewById<TextView>(R.id.category_name)
        val image_category = itemView.findViewById<ImageView>(R.id.image_category)
        val category_layout = itemView.findViewById<LinearLayout>(R.id.category_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.categories_layout, parent, false)
        return holder(view)
    }
    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.category_name.text = list?.get(position)?.name
        Glide.with(holder.image_category.context).load(list?.get(position)?.icon).into(holder.image_category)
        holder.category_layout.setOnClickListener {
            listner.onCategoryChanged(list?.get(position)?.id)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}
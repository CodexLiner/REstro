package com.arunbamniya.restro.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R
import com.arunbamniya.restro.interfaces.AdapterClicker
import com.arunbamniya.restro.network.CategoryResponse
import com.bumptech.glide.Glide

class CategoryAdapter(val list: List<CategoryResponse>?, private val listener: AdapterClicker) :
    RecyclerView.Adapter<CategoryAdapter.holder>() {
    private var index = 0

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val category_name: TextView = itemView.findViewById<TextView>(R.id.category_name)
        val image_category: ImageView = itemView.findViewById<ImageView>(R.id.image_category)
        val category_layout: LinearLayoutCompat = itemView.findViewById<LinearLayoutCompat>(R.id.category_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.categories_layout, parent, false)
        return holder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.category_name.text = list?.get(position)?.name
        Glide.with(holder.image_category.context).load(list?.get(position)?.icon)
            .into(holder.image_category)

        holder.category_layout.setOnClickListener {
            for (i in list?.indices!!) {
                list[i].isSelected = true
            }

            listener.onCategoryChanged(list?.get(position)?.id, position)
        }
        if (list?.get(position)?.isSelected == true) {
            holder.category_layout.background =
                holder.category_layout.resources.getDrawable(R.drawable.categories_bg_selected)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}
package com.arunbamniya.restro.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R
import com.arunbamniya.restro.interfaces.AdapterClicker
import com.arunbamniya.restro.network.ItemResponse
import com.bumptech.glide.Glide

class items_adapter(val list: MutableList<ItemResponse>?, val listner: AdapterClicker) :
    RecyclerView.Adapter<items_adapter.holder>() {

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_image = itemView.findViewById<ImageView>(R.id.item_image)
        val item_name = itemView.findViewById<TextView>(R.id.item_name)
        val item_price = itemView.findViewById<TextView>(R.id.item_price)
        val item_button = itemView.findViewById<Button>(R.id.item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.items_layout_single, parent, false)
        return holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: holder, position: Int) {
        Glide.with(holder.item_image.context).load(list?.get(position)?.photo)
            .placeholder(R.drawable.food_place).into(holder.item_image)
        holder.item_name.text = list?.get(position)?.name
        holder.item_price.text = "₹ ${list?.get(position)?.price}"
        holder.item_button.isEnabled = list?.get(position)?.isAdded != true

        holder.item_button.setOnClickListener {
            list?.get(position)?.isAdded = true
            listner.onItemClicked(list?.get(position), position)

        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}
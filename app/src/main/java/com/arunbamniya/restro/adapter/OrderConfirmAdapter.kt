package com.arunbamniya.restro.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R
import com.arunbamniya.restro.network.ItemResponse

class OrderConfirmAdapter(val list: MutableList<ItemResponse>?) :
    RecyclerView.Adapter<OrderConfirmAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_count = itemView.findViewById<TextView>(R.id.item_count)
        val item_name = itemView.findViewById<TextView>(R.id.item_name)
        val item_price = itemView.findViewById<TextView>(R.id.item_price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_confirm_layout, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val price = list?.get(position)?.price?.toInt()?.times(list.get(position).itemCount)
        holder.item_price.text = "₹ $price"
        holder.item_name.text = list?.get(position)?.name
        holder.item_count.text = list?.get(position)?.itemCount.toString()

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}
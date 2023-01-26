package com.arunbamniya.restro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R
import com.arunbamniya.restro.interfaces.AdapterClicker
import com.arunbamniya.restro.network.ItemResponse

class my_cart(var list: MutableList<ItemResponse>?, val listener: AdapterClicker) :
    RecyclerView.Adapter<my_cart.holder>() {

    class holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_name = itemView.findViewById<TextView>(R.id.item_name)
        val item_price = itemView.findViewById<TextView>(R.id.item_price)
        val item_count = itemView.findViewById<TextView>(R.id.item_count)
        val minus_button = itemView.findViewById<ImageView>(R.id.minus_button)
        val plus_button = itemView.findViewById<ImageView>(R.id.plus_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item_design, parent, false)
        return holder(view)
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        listener.upDateCartValue(true)
        holder.item_name.text = list?.get(position)?.name
        val price = "₹ "+list?.get(position)?.price
        holder.item_price.text = price
        holder.item_name.text = list?.get(position)?.name
        holder.item_count.text = list?.get(position)?.itemCount.toString()

        holder.minus_button.setOnClickListener {
            try {
                if (list?.size == 1) {
                    listener.upDateCartValue(false)
                } else {
                    listener.upDateCartValue(true)
                }
                if (list?.get(position)?.itemCount == 1) {
                    list?.get(position)?.isAdded = false
                    listener.onCartChanged(list?.get(position), position)
                } else {
                    list?.get(position)?.itemCount = list?.get(position)?.itemCount?.minus(1)!!
                    this.notifyItemChanged(position)
                }
            } catch (_: Exception) {
            }
        }
        holder.plus_button.setOnClickListener {
            listener.upDateCartValue(true)
            list?.get(position)?.itemCount = list?.get(position)?.itemCount?.plus(1)!!
            this.notifyItemChanged(position)
        }

    }


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}
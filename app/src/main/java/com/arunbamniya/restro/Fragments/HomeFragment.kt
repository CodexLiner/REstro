package com.arunbamniya.restro.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R
import com.arunbamniya.restro.adapter.category_adapter
import com.arunbamniya.restro.adapter.items_adapter
import com.arunbamniya.restro.adapter.my_cart
import com.arunbamniya.restro.interfaces.AdapterClicker
import com.arunbamniya.restro.network.CategoryResponse
import com.arunbamniya.restro.network.ItemResponse
import com.arunbamniya.restro.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(), AdapterClicker {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var items_recycler: RecyclerView
    lateinit var items_adapter: items_adapter

    lateinit var category_recycler: RecyclerView
    lateinit var categories_adapter: category_adapter

    lateinit var cart_recycler: RecyclerView
    lateinit var cart_adapter: my_cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        items_recycler = view.findViewById(R.id.items_recycler);

        category_recycler = view.findViewById(R.id.category_recycler);

        cart_recycler = view.findViewById(R.id.cart_recycler);
        cart_adapter = my_cart(null, this@HomeFragment)


        view.findViewById<Button>(R.id.pay_button).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_frame, QRFragment())?.addToBackStack("home")?.commit()
        }
        getCategories()
        getItems("63c2b895c6f1a0528151050f")

        items_recycler.layoutManager = GridLayoutManager(context, 2)

        category_recycler.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )

        cart_recycler.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        return view;
    }

    private fun getItems(category: String) {
        val apiInterface = RetrofitClient.getInstance().api

        val responseCall = apiInterface.getItems(category)
        responseCall?.enqueue(object : Callback<MutableList<ItemResponse>?> {
            override fun onResponse(
                call: Call<MutableList<ItemResponse>?>,
                response: Response<MutableList<ItemResponse>?>
            ) {
                items_adapter = items_adapter(response.body(), this@HomeFragment)
                items_recycler.adapter = items_adapter
            }

            override fun onFailure(call: Call<MutableList<ItemResponse>?>, t: Throwable) {
                Toast.makeText(context, "Failed To Load Items", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getCategories() {
        val apiInterface = RetrofitClient.getInstance().api
        val responseCall = apiInterface.listCategory()
        responseCall?.enqueue(object : Callback<List<CategoryResponse>?> {
            override fun onResponse(
                call: Call<List<CategoryResponse>?>, response: Response<List<CategoryResponse>?>
            ) {
                categories_adapter = category_adapter(response.body(), this@HomeFragment)
                category_recycler.adapter = categories_adapter
            }

            override fun onFailure(call: Call<List<CategoryResponse>?>, t: Throwable) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    override fun onItemClicked(
        item: ItemResponse, position: Int
    ) {
        if (cart_adapter.list?.isNotEmpty() == true) {
            cart_adapter.list!!.add(item)
        } else {
            val mList: MutableList<ItemResponse> = mutableListOf<ItemResponse>()
            mList.add(item)
            cart_adapter = my_cart(mList, this@HomeFragment)
        }

        items_adapter.notifyItemChanged(position)
        cart_recycler.adapter = cart_adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCartChanged(
        item: ItemResponse, position: Int
    ) {
        items_adapter.list?.get(position)?.isAdded = false
//        items_adapter.notifyItemChanged(position)
        items_adapter.notifyDataSetChanged()

        cart_adapter.list?.removeAt(position)
        cart_adapter.list?.let { cart_adapter.notifyItemRangeChanged(position, it.size) }
        cart_adapter.notifyDataSetChanged()
    }

    override fun onCategoryChanged(category: String?) {
        if (category != null) {
            getItems(category)
        }
    }
}
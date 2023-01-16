package com.arunbamniya.restro.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R
import com.arunbamniya.restro.adapter.CategoryAdapter
import com.arunbamniya.restro.adapter.OrderConfirmAdapter
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
    lateinit var categories_adapter: CategoryAdapter

    lateinit var cart_recycler: RecyclerView
    lateinit var cart_adapter: my_cart

    lateinit var cart_value: AppCompatTextView

    lateinit var pay_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        items_recycler = view.findViewById(R.id.items_recycler);

        cart_value = view.findViewById(R.id.cart_value);
        pay_button = view.findViewById<Button>(R.id.pay_button)
        category_recycler = view.findViewById(R.id.category_recycler);

        cart_recycler = view.findViewById(R.id.cart_recycler);
        cart_adapter = my_cart(null, this@HomeFragment)


        pay_button.setOnClickListener {


            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels

            val dialog = context?.let { it1 -> Dialog(it1) }
            dialog?.setContentView(R.layout.custom_cart_dialog)

            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.setCancelable(false)
            dialog?.window?.setLayout((width / 2) + 100, height - 150)

            val confirm_recycler = dialog?.findViewById<RecyclerView>(R.id.confirm_recycler)
            val confirm = dialog?.findViewById<Button>(R.id.confirm_button)
            val cancel = dialog?.findViewById<Button>(R.id.cancel_button)
            confirm_recycler?.layoutManager =
                LinearLayoutManager(dialog?.context, LinearLayoutManager.VERTICAL, false)
            confirm_recycler?.adapter = OrderConfirmAdapter(cart_adapter.list)
            val sum = sumOfvalues(cart_adapter.list, 0)
            confirm?.text = "Pay ₹ $sum"
            cancel?.setOnClickListener { dialog.dismiss() }

            confirm?.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_frame, QRFragment(sum , cart_adapter.list))?.addToBackStack("home")?.commit()
            }

            dialog?.show()


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
                response.body()?.get(0)?.isSelected = true
                categories_adapter = CategoryAdapter(response.body(), this@HomeFragment)
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

        pay_button.isEnabled = cart_adapter.list?.size!! > 0

    }

    private fun sumOfvalues(list: MutableList<ItemResponse>?): StringBuilder {
        var mSum = 0;
        for (i in list?.indices!!) {
            mSum += list[i].price.toInt() * list[i].itemCount.toInt()
        }
        return StringBuilder("Cart $mSum")
    }

    private fun sumOfvalues(list: MutableList<ItemResponse>?, int: Int): Int {
        var mSum = 0;
        for (i in list?.indices!!) {
            mSum += list[i].price.toInt() * list[i].itemCount.toInt()
        }
        return mSum
    }

    override fun onCategoryChanged(category: String?, position: Int) {
        if (category != null) {
            getItems(category)
            setNotSelected(0, null)
            categories_adapter.list?.get(position)?.isSelected = true
            categories_adapter = CategoryAdapter(categories_adapter.list, this@HomeFragment)
            category_recycler.adapter = categories_adapter
        }
    }

    override fun upDateCartValue() {

        pay_button.isEnabled = cart_adapter.list?.size!! > 0
        val value = sumOfvalues(cart_adapter.list).toString()
        cart_value.text = value.toString()
    }

    private fun setNotSelected(position: Int, list: List<CategoryResponse>?) {
        for (i in categories_adapter.list?.indices!!) {
            categories_adapter.list?.get(i)?.isSelected = false
        }
    }
}
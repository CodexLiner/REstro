package com.arunbamniya.restro.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Parcelable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arunbamniya.restro.Constants
import com.arunbamniya.restro.R
import com.arunbamniya.restro.activities.HomeActivity
import com.arunbamniya.restro.activities.MainActivity
import com.arunbamniya.restro.network.ItemResponse
import com.arunbamniya.restro.network.QResponse
import com.arunbamniya.restro.network.RetrofitClient
import com.arunbamniya.restro.network.orders
import retrofit2.Call
import retrofit2.Response
import java.io.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class QRFragment(private val sum: Int, val list: MutableList<ItemResponse>?) : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var date: TextView
    lateinit var time_out: TextView
    lateinit var qr_code: ImageView
    lateinit var time: TextView
    lateinit var head_text: TextView
    lateinit var dialog: Dialog
    private var orderStatus = false


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
        val view: View = inflater.inflate(R.layout.fragment_q_r, container, false)

        dialog = activity?.let { Dialog(it) }!!

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window?.setLayout(350, ViewGroup.LayoutParams.WRAP_CONTENT)


        dialog.show()
        getQRCode()

        time_out = view.findViewById(R.id.time_out_in);
        head_text = view.findViewById(R.id.head_text);

        qr_code = view.findViewById<ImageView>(R.id.qr_code)



        view.findViewById<TextView>(R.id.cancel_button).setOnClickListener {
            activity?.supportFragmentManager?.popBackStack(
                "home", FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
        return view
    }

    private fun getQRCode() {
        val body: ArrayList<orders> = ArrayList()
        for (i in list?.indices!!) {
            body.add(orders(list[i].id, list[i].itemCount))
        }

        val apiInterface = RetrofitClient.getInstance().api
        val responseCall = apiInterface.getQRCode(body)

        responseCall?.enqueue(object : retrofit2.Callback<QResponse> {
            override fun onResponse(
                call: retrofit2.Call<QResponse>, response: Response<QResponse>
            ) {
                if (response.body() != null && response.body()!!.qr_image != null) {
                    setBitmapImage(response.body()!!.qr_image)
                    startTimer(response.body()!!.id)
                }
            }

            override fun onFailure(call: retrofit2.Call<QResponse>, t: Throwable) {

            }

        })

//        val paytmParams = JSONObject()
//        paytmParams.put("amount", sum)
//        val post_data = paytmParams.toString()
//        val url = URL("https://paytm-qr.vercel.app/")
//        val okHttpClient = OkHttpClient()
//        val requestBody =
//            post_data.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
//        val request = okhttp3.Request.Builder()
//            .method("POST", requestBody)
//            .url(url = url)
//            .build()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {}
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                // Handle this
//
//                val jsonObject: JSONObject? = response.body?.string()?.let { JSONObject(it) }
//                if (jsonObject != null) {
//                    val bodyObject: JSONObject = JSONObject(jsonObject.getString("body"))
//                    if (bodyObject.getString("image") != null) {
//                        setBitmapImage(bodyObject.getString("image"))
//                        startTimer()
//                    }
//                }
//            }
//        })
    }

    private fun transactionStatus(id: String) {
        val timer = object : CountDownTimer(120000, 3000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                if (!orderStatus) {
                    val apiInterface = RetrofitClient.getInstance().api
                    val responseCall = apiInterface.getStatus(id)
                    responseCall?.enqueue(object : retrofit2.Callback<ItemResponse> {
                        override fun onResponse(
                            call: Call<ItemResponse>,
                            response: Response<ItemResponse>
                        ) {
                            Log.d("TAG", "onResponse = id = ${id}: ${response.body()?.order_status.toString()}")
                            if (response.body()?.order_status == Constants.PAYMENT_STATUS_SUCCESS) {
                                orderStatus = true
                                orderSuccess(id)
                            }
                            else if (response.body()?.order_status == Constants.PAYMENT_STATUS_FAILED) {
                                activity?.runOnUiThread {
                                    qr_code.setImageResource(R.drawable.undraw_feeling_blue__4_b7q)
                                    time_out.visibility = View.GONE
                                    head_text.text = Constants.TRANSACTION_FAILED
                                    Handler().postDelayed({
                                        activity?.supportFragmentManager?.popBackStack()
                                    }, 3000)

                                }
                            }
                        }

                        override fun onFailure(call: Call<ItemResponse>, t: Throwable) {

                        }
                    })
                }
            }

            override fun onFinish() {
                if (!orderStatus) {
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        }
        timer.start()
    }

    private fun orderSuccess(id: String) {
        val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
        val usbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val action = intent.action
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized(this) {
                        val usbManager =
                            activity?.getSystemService(Context.USB_SERVICE) as UsbManager?
                        val usbDevice =
                            intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice?
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (usbManager != null && usbDevice != null) {
                                Toast.makeText(context, "usb permitted", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }



        Toast.makeText(context, "order success", Toast.LENGTH_SHORT).show()
    }

    private fun startTimer(id: String) {
        activity?.runOnUiThread {
            var delay = 10;
//          Timer To auto cancel order
            val timer = object : CountDownTimer(120000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    delay -= 1
                    time_out.text = "Code Expires In ${millisUntilFinished / 1000} Seconds"
                    if (delay == 0) {
                        transactionStatus(id)
                    }
                }

                override fun onFinish() {
                    if (!orderStatus) {
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
            }
            timer.start()
        }
    }

    private fun setBitmapImage(s: String) {
        val imageBytes = Base64.decode(s, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        activity?.runOnUiThread {
            qr_code.setImageBitmap(decodedImage)
            dialog.dismiss()
        }
    }
}
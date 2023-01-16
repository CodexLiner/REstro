package com.arunbamniya.restro.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.arunbamniya.restro.R
import com.arunbamniya.restro.network.ItemResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.*
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class QRFragment(val sum: Int, val list: MutableList<ItemResponse>?) : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var date: TextView
    lateinit var time_out: TextView
    lateinit var qr_code: ImageView
    lateinit var time: TextView
    lateinit var dialog: Dialog


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
        sendcall()

        time_out = view.findViewById(R.id.time_out_in);

        qr_code = view.findViewById<ImageView>(R.id.qr_code)



        view.findViewById<TextView>(R.id.cancel_button).setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        return view
    }

    private fun sendcall() {

        val paytmParams = JSONObject()
        paytmParams.put("amount", sum)
        val post_data = paytmParams.toString()


        val url = URL("https://paytm-qr.vercel.app/")
        val okHttpClient = OkHttpClient()
        val requestBody =
            post_data.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = okhttp3.Request.Builder()
            .method("POST", requestBody)
            .url(url = url)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: okhttp3.Response) {
                // Handle this

                val jsonObject: JSONObject? = response.body?.string()?.let { JSONObject(it) }
                if (jsonObject != null) {
                    val bodyObject: JSONObject = JSONObject(jsonObject.getString("body"))
                    if (bodyObject.getString("image") != null) {
                        setBitmapImage(bodyObject.getString("image"))
                        startTimer()
                    }
                }
            }
        })
    }

    private fun startTimer() {
        activity?.runOnUiThread {
            //        Timer To auto cancel order
            val timer = object : CountDownTimer(120000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    time_out.text = "Code Expires In ${millisUntilFinished / 1000} Seconds"
                }

                override fun onFinish() {
                    activity?.supportFragmentManager?.popBackStack()
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

//    companion object {
//
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) = QRFragment(sum, cart_adapter.list).apply {
//            arguments = Bundle().apply {
//                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
//            }
//        }
//    }
}
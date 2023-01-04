package com.arunbamniya.restro.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.arunbamniya.restro.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

//        val activity : Activity = this@MainActivity
//        val manager = activity
//            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
//
//        val localLayoutParams = WindowManager.LayoutParams()
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
//        localLayoutParams.gravity = Gravity.TOP
//        localLayoutParams.flags =
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or  // this is to enable the notification to recieve touch events
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or  // Draws over status bar
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//
//        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
//        localLayoutParams.height = (50 * resources
//            .displayMetrics.scaledDensity).toInt()
//        localLayoutParams.format = PixelFormat.TRANSPARENT
//
//        val view = customViewGroup(activity)
//
//        manager.addView(view, localLayoutParams)
    }
    class customViewGroup(context: Context?) : ViewGroup(context) {
        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
        override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
            Log.d("customViewGroup", "**********Intercepted")
            return true
        }
    }
}
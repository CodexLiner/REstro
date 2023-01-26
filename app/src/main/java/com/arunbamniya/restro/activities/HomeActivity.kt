package com.arunbamniya.restro.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.Fragments.HomeFragment
import com.arunbamniya.restro.Fragments.WelcomeFragment
import com.arunbamniya.restro.R
import com.arunbamniya.restro.adapter.items_adapter
import com.arunbamniya.restro.kiosk.KioskUtil
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeActivity : AppCompatActivity() {

    lateinit var date: TextView
    lateinit var time: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        setDateTime(date, time)
        date.setOnClickListener {
            KioskUtil.startKioskMode(this)
        }
        this.supportFragmentManager.beginTransaction().replace(R.id.main_frame, HomeFragment())
            .commit()

    }

    override fun onBackPressed() {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDateTime(date: TextView?, time: TextView?) {
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        sleep(1000)
                        runOnUiThread {
                            if (date != null) {
                                date.text = LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("EEEE d MMM"))
                            }
                            if (time != null) {
                                time.text =
                                    LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm a"))
                            }
                        }
                    }
                } catch (_: InterruptedException) {
                }
            }
        }
        thread.start()
    }
}
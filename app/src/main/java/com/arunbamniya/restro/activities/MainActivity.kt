package com.arunbamniya.restro.activities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.arunbamniya.restro.Fragments.QRFragment
import com.arunbamniya.restro.Fragments.WelcomeFragment
import com.arunbamniya.restro.R
import com.arunbamniya.restro.kiosk.KioskUtil
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    lateinit var date: TextView
    lateinit var time_out: TextView
    lateinit var time: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        time = findViewById(R.id.time);
        date = findViewById(R.id.date);

        date.setOnClickListener {
            KioskUtil.startKioskMode(this)
        }

        setDateTime(date, time)

        this.supportFragmentManager.beginTransaction().replace(R.id.fmm, WelcomeFragment()).commit()

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

    override fun onBackPressed() {

    }


}
package com.arunbamniya.restro.activities

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.arunbamniya.restro.Fragments.QRFragment
import com.arunbamniya.restro.R
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ContainerActivity : AppCompatActivity() {
    lateinit var date: TextView
    lateinit var time_out: TextView
    lateinit var time: TextView
    var time_out_in = 120;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_container)

        this.supportFragmentManager.beginTransaction().replace(R.id.main_frame, QRFragment())
            .commit()

        time = findViewById(R.id.time);
        date = findViewById(R.id.date);

        setDateTime(date, time)


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
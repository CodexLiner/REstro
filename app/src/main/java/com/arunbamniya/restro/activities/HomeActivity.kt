package com.arunbamniya.restro.activities

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arunbamniya.restro.R
import com.arunbamniya.restro.adapter.items_adapter
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var items_recycler: RecyclerView
    lateinit var items_adapter: items_adapter
    lateinit var date: TextView
    lateinit var time: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        items_recycler = findViewById(R.id.items_recycler);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);

        setDateTime(date, time)


        items_recycler.layoutManager = GridLayoutManager(applicationContext, 2)
        items_adapter = items_adapter()
        items_recycler.adapter = items_adapter


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
                } catch (_: InterruptedException) { }
            }
        }

        thread.start()
    }
}
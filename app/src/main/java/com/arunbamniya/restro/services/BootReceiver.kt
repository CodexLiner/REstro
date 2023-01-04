package com.arunbamniya.restro.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arunbamniya.restro.activities.MainActivity

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {
        val myIntent = Intent(context, MainActivity::class.java)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(myIntent)
    }
}
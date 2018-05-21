package com.markantoni.linies.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class TimeZoneReceiver(private val action: () -> Unit) : BroadcastReceiver() {
    private var registered = false

    fun register(context: Context) {
        if (!registered) {
            context.registerReceiver(this, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
            registered = true
        }
    }

    fun unregister(context: Context) {
        registered = false
        context.unregisterReceiver(this)
    }

    override fun onReceive(context: Context, intent: Intent) = action()

}
package com.markantoni.linies.util

import android.util.Log
import com.markantoni.linies.BuildConfig

private object Logger {
    val DEBUG = BuildConfig.DEBUG
    val TAG = "Linies"
}

fun logd(message: Any) {
    if (Logger.DEBUG) Log.d(Logger.TAG, "$message")
}
package com.markantoni.linies.common.util

import android.util.Log
import com.markantoni.linies.common.BuildConfig

private object Logger {
    val DEBUG = BuildConfig.DEBUG
    val TAG = "Linies"
}

fun logd(message: Any) {
    if (Logger.DEBUG) Log.d(Logger.TAG, "$message")
}
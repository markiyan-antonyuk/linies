package com.markantoni.linies.common.util

import android.util.Log
import com.markantoni.linies.common.BuildConfig

object Logger {
    val DEBUG = BuildConfig.DEBUG
    val TAG = "Linies"
    var APP_TYPE = "Undefined"

    fun init(appType: String) {
        APP_TYPE = appType
    }
}

fun logd(message: Any?) {
    if (Logger.DEBUG) Log.d("${Logger.TAG} [${Logger.APP_TYPE}]", "$message")
}
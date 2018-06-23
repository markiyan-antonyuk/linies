package com.markantoni.linies.util

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import com.markantoni.linies.LiniesWatchFaceService
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.preferences.Preferences

fun Context.getWatchFaceServiceComponentName() = ComponentName(this, LiniesWatchFaceService::class.java)

fun Bundle.withConfiguration(context: Context, setup: Configuration.() -> Unit = {}) {
    Preferences.configuration(context)
            .also { it.setup() }
            .write(::putInt, ::putBoolean, ::putString)
}

fun configurationFrom(context: Context, setup: Configuration.() -> Unit = {}) =
        Preferences.configuration(context).apply { setup() }
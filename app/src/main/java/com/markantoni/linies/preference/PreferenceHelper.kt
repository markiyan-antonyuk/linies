package com.markantoni.linies.preference

import android.content.Context
import android.graphics.Color
import com.markantoni.linies.Key

object PreferenceHelper {
    private val DEFAULT_COLOR = Color.parseColor("#9e9e9e")
    private val PREF_NAME_TYPE = "pref.type."

    private fun getPreferencesFor(context: Context, type: Int) = context.getSharedPreferences("$PREF_NAME_TYPE$type", Context.MODE_PRIVATE)

    fun update(context: Context, type: Int, color: Int, visible: Boolean) {
        getPreferencesFor(context, type).edit().apply {
            putInt(Key.COLOR, color)
            putBoolean(Key.VISIBLE, visible)
            apply()
        }
    }

    fun getColor(context: Context, type: Int) = getPreferencesFor(context, type).getInt(Key.COLOR, DEFAULT_COLOR)
    fun isVisible(context: Context, type: Int) = getPreferencesFor(context, type).getBoolean(Key.VISIBLE, true)
}
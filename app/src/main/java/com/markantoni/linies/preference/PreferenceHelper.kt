package com.markantoni.linies.preference

import android.content.Context
import android.graphics.Color
import com.markantoni.linies.Key
import com.markantoni.linies.R

object PreferenceHelper {
    private val PREF_NAME_TYPE = "pref.type."

    private fun getPreferencesFor(context: Context, type: Int) = context.getSharedPreferences("$PREF_NAME_TYPE$type", Context.MODE_PRIVATE)

    fun update(context: Context, type: Int, color: Int, visible: Boolean, hours24: Boolean) {
        getPreferencesFor(context, type).edit().apply {
            putInt(Key.COLOR, color)
            putBoolean(Key.VISIBLE, visible)
            putBoolean(Key.HOURS24, hours24)
            apply()
        }
    }

    fun getColor(context: Context, type: Int) = getPreferencesFor(context, type).getInt(Key.COLOR, Color.parseColor(context.resources.getStringArray(R.array.color_values)[0]))
    fun isVisible(context: Context, type: Int) = getPreferencesFor(context, type).getBoolean(Key.VISIBLE, true)
    fun is24Hours(context: Context, type: Int) = getPreferencesFor(context, type).getBoolean(Key.HOURS24, true)
}
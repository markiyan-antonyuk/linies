@file:Suppress("DEPRECATION")

package com.markantoni.linies.preference

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager
import com.markantoni.linies.Key
import com.markantoni.linies.R

@Deprecated("Soon to be removed")
class WatchFacePreferences(private val context: Context) : Preferences {

    private val PREF_NAME_TYPE = "pref.type."

    private val defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private fun getPreferencesFor(type: Int) = context.getSharedPreferences("$PREF_NAME_TYPE$type", Context.MODE_PRIVATE)

    private fun editPreferencesFor(type: Int, editBlock: SharedPreferences.Editor.() -> SharedPreferences.Editor)
            = getPreferencesFor(type).edit().editBlock().apply()

    private fun editDefaultPreferences(editBlock: SharedPreferences.Editor.() -> SharedPreferences.Editor)
            = defaultPreferences.edit().editBlock().apply()

    override fun setColor(type: Int, color: Int) = editPreferencesFor(type) { putInt(Key.COLOR, color) }

    override fun setVisible(type: Int, visible: Boolean) = editPreferencesFor(type) { putBoolean(Key.VISIBLE, visible) }

    override fun set24Hours(hours24: Boolean) = editDefaultPreferences { putBoolean(Key.HOURS24, hours24) }

    override fun setDateFormat(dateFormat: String) = editDefaultPreferences { putString(Key.DATE_FORMAT, dateFormat) }

    override fun setAnimating(animate: Boolean) = editDefaultPreferences { putBoolean(Key.ANIMATING, animate) }

    override fun getColor(type: Int) = getPreferencesFor(type).getInt(Key.COLOR, Color.parseColor(context.resources.getStringArray(R.array.color_values)[0]))

    override fun isVisible(type: Int) = getPreferencesFor(type).getBoolean(Key.VISIBLE, true)

    override fun is24Hours(): Boolean = defaultPreferences.getBoolean(Key.HOURS24, true)

    override fun getDateFormat(): String = defaultPreferences.getString(Key.DATE_FORMAT, context.resources.getStringArray(R.array.date_formats)[0])

    override fun isAnimating() = defaultPreferences.getBoolean(Key.ANIMATING, true)
}
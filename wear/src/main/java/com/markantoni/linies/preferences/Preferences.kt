@file:Suppress("DEPRECATION")

package com.markantoni.linies.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.markantoni.linies.util.Type
import com.markantoni.linies.common.configuration.*
import com.markantoni.linies.preference.WatchFacePreferences


class Preferences(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private lateinit var editor: SharedPreferences.Editor

    private val oldPrefs = WatchFacePreferences(context)

    var configuration: Configuration
        get() = Configuration.read({
            preferences.getInt(it, when (it) {
                Second.COLOR -> oldPrefs.getColor(Type.SECOND)
                Minute.COLOR -> oldPrefs.getColor(Type.MINUTE)
                Hour.COLOR -> oldPrefs.getColor(Type.HOUR)
                Digital.COLOR -> oldPrefs.getColor(Type.DIGITAL)
                Date.COLOR -> oldPrefs.getColor(Type.DATE)
                Complication.COLOR -> oldPrefs.getColor(Type.COMPLICATIONS)
                else -> 0
            })
        }, {
            preferences.getBoolean(it, when (it) {
                Digital.VISIBLE -> oldPrefs.isVisible(Type.DIGITAL)
                Digital.IS24 -> oldPrefs.is24Hours()
                Date.VISIBLE -> oldPrefs.isVisible(Type.DATE)
                Animation.ENABLED -> oldPrefs.isAnimating()
                else -> false
            })
        }, {
            preferences.getString(it, when (it) {
                Date.FORMAT -> oldPrefs.getDateFormat()
                else -> ""
            })
        })
        set(value) {
            preferences.edit().apply {
                value.write(
                        { k, v -> putInt(k, v) },
                        { k, v -> putBoolean(k, v) },
                        { k, v -> putString(k, v) })
                apply()
            }
        }

    companion object {
        fun configuration(context: Context) = Preferences(context).configuration
    }
}

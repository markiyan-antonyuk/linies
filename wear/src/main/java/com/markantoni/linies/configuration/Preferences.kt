package com.markantoni.linies.configuration

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.markantoni.linies.Type
import com.markantoni.linies.preference.WatchFacePreferences


class Preferences(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private lateinit var editor: SharedPreferences.Editor

    @Suppress("DEPRECATION")
    private val oldPrefs = WatchFacePreferences(context)

    @Suppress("DEPRECATION")
    var configuration: Configuration
        get() = Configuration(
                Second(Second.COLOR.readInt(oldPrefs.getColor(Type.SECOND))),
                Minute(Minute.COLOR.readInt(oldPrefs.getColor(Type.MINUTE))),
                Hour(Hour.COLOR.readInt(oldPrefs.getColor(Type.HOUR))),
                Digital(Digital.COLOR.readInt(oldPrefs.getColor(Type.DIGITAL)), Digital.VISIBLE.readBoolean(oldPrefs.isVisible(Type.DIGITAL)), Digital.IS24.readBoolean(oldPrefs.is24Hours())),
                Date(Date.COLOR.readInt(oldPrefs.getColor(Type.DATE)), Date.VISIBLE.readBoolean(oldPrefs.isVisible(Type.DATE)), Date.FORMAT.readString(oldPrefs.getDateFormat())),
                Complication(Complication.COLOR.readInt(oldPrefs.getColor(Type.COMPLICATIONS))),
                Animation(Animation.ENABLED.readBoolean(oldPrefs.isAnimating()))
        )
        set(value) {
            editor = preferences.edit()
            value.apply {
                second.color.write(Second.COLOR)
                minute.color.write(Minute.COLOR)
                hour.color.write(Hour.COLOR)

                digital.color.write(Digital.COLOR)
                digital.visible.write(Digital.VISIBLE)
                digital.is24.write(Digital.IS24)

                date.color.write(Date.COLOR)
                date.visible.write(Date.VISIBLE)
                date.format.write(Date.FORMAT)

                animation.enabled.write(Animation.ENABLED)
                complication.color.write(Complication.COLOR)
            }
            editor.apply()
        }

    private fun String.readInt(default: Int) = preferences.getInt(this, default)
    private fun String.readBoolean(default: Boolean) = preferences.getBoolean(this, default)
    private fun String.readString(default: String) = preferences.getString(this, default)

    private fun Int.write(key: String) = editor.putInt(key, this)
    private fun Boolean.write(key: String) = editor.putBoolean(key, this)
    private fun String.write(key: String) = editor.putString(key, this)

    companion object {
        fun configuration(context: Context) = Preferences(context).configuration
    }
}

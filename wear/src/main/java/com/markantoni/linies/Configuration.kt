package com.markantoni.linies

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import android.preference.PreferenceManager
import android.support.wearable.complications.ComplicationData
import com.markantoni.linies.preference.WatchFacePreferences
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Configuration(
        var second: Second,
        var minute: Minute,
        var hour: Hour,
        var digital: Digital,
        var date: Date,
        var animation: Animation,
        var complication: Complication
) : Parcelable

@Parcelize
data class Second(var color: Int) : Parcelable {
    companion object {
        const val COLOR = "second.color"
    }
}

@Parcelize
data class Minute(var color: Int) : Parcelable {
    companion object {
        const val COLOR = "minute.color"
    }
}

@Parcelize
data class Hour(var color: Int, var is24: Boolean) : Parcelable {
    companion object {
        const val COLOR = "hour.color"
        const val IS24 = "hour.is24"
    }
}

@Parcelize
data class Digital(var color: Int, var visible: Boolean) : Parcelable {
    companion object {
        const val COLOR = "digital.color"
        const val VISIBLE = "digital.visible"
    }
}

@Parcelize
data class Date(var color: Int, var visible: Boolean, var format: String) : Parcelable {
    companion object {
        const val COLOR = "date.color"
        const val VISIBLE = "date.visible"
        const val FORMAT = "date.format"
    }
}

@Parcelize
data class Animation(var enabled: Boolean) : Parcelable {
    companion object {
        const val ENABLED = "animation.enabled"
    }
}

@Parcelize
data class Complication(var color: Int) : Parcelable {
    companion object {
        const val COLOR = "complication.color"

        const val CENTER = 0
        const val BOTTOM = 1

        val IDS = intArrayOf(CENTER, BOTTOM)
        val SUPPORTED_TYPES = mapOf(
                CENTER to intArrayOf(ComplicationData.TYPE_ICON, ComplicationData.TYPE_LONG_TEXT, ComplicationData.TYPE_RANGED_VALUE),
                BOTTOM to intArrayOf(ComplicationData.TYPE_SHORT_TEXT, ComplicationData.TYPE_RANGED_VALUE)
        )
    }
}

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
                Hour(Hour.COLOR.readInt(oldPrefs.getColor(Type.HOUR)), Hour.IS24.readBoolean(oldPrefs.is24Hours())),
                Digital(Digital.COLOR.readInt(oldPrefs.getColor(Type.DIGITAL)), Digital.VISIBLE.readBoolean(oldPrefs.isVisible(Type.DIGITAL))),
                Date(Date.COLOR.readInt(oldPrefs.getColor(Type.DATE)), Date.VISIBLE.readBoolean(oldPrefs.isVisible(Type.DATE)), Date.FORMAT.readString(oldPrefs.getDateFormat())),
                Animation(Animation.ENABLED.readBoolean(oldPrefs.isAnimating())),
                Complication(Complication.COLOR.readInt(oldPrefs.getColor(Type.COMPLICATIONS)))
        )
        set(value) {
            editor = preferences.edit()
            value.apply {
                second.color.write(Second.COLOR)
                minute.color.write(Minute.COLOR)

                hour.color.write(Hour.COLOR)
                hour.is24.write(Hour.IS24)

                digital.color.write(Digital.COLOR)
                digital.visible.write(Digital.VISIBLE)

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
}
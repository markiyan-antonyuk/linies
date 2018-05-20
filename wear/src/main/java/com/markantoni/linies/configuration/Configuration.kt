package com.markantoni.linies.configuration

import android.os.Parcelable
import android.support.wearable.complications.ComplicationData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Configuration(
        var second: Second,
        var minute: Minute,
        var hour: Hour,
        var digital: Digital,
        var date: Date,
        var complication: Complication,
        var animation: Animation
) : Parcelable

open class Hand(open var color: Int)
open class VisibleHand(override var color: Int, open var visible: Boolean) : Hand(color)

@Parcelize
data class Second(override var color: Int) : Hand(color), Parcelable {
    companion object {
        const val COLOR = "second.color"
    }
}

@Parcelize
data class Minute(override var color: Int) : Hand(color), Parcelable {
    companion object {
        const val COLOR = "minute.color"
    }
}

@Parcelize
data class Hour(override var color: Int) : Hand(color), Parcelable {
    companion object {
        const val COLOR = "hour.color"
    }
}

@Parcelize
data class Digital(override var color: Int, override var visible: Boolean, var is24: Boolean) : VisibleHand(color, visible), Parcelable {
    companion object {
        const val COLOR = "digital.color"
        const val VISIBLE = "digital.visible"
        const val IS24 = "hour.is24"
    }
}

@Parcelize
data class Date(override var color: Int, override var visible: Boolean, var format: String) : VisibleHand(color, visible), Parcelable {
    companion object {
        const val COLOR = "date.color"
        const val VISIBLE = "date.visible"
        const val FORMAT = "date.format"
    }
}

@Parcelize
data class Complication(override var color: Int) : Hand(color), Parcelable {
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

@Parcelize
data class Animation(var enabled: Boolean) : Parcelable {
    companion object {
        const val ENABLED = "animation.enabled"
    }
}
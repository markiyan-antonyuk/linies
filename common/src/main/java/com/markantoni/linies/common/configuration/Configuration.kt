package com.markantoni.linies.common.configuration

import android.os.Parcelable
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
) : Parcelable {

    fun write(writeInt: (String, Int) -> Unit,
              writeBoolean: (String, Boolean) -> Unit,
              writeString: (String, String) -> Unit) {
        writeInt(Second.COLOR, second.color)
        writeInt(Second.COLOR, second.color)
        writeInt(Minute.COLOR, minute.color)
        writeInt(Hour.COLOR, hour.color)

        writeInt(Digital.COLOR, digital.color)
        writeBoolean(Digital.VISIBLE, digital.visible)
        writeBoolean(Digital.IS24, digital.is24)

        writeInt(Date.COLOR, date.color)
        writeBoolean(Date.VISIBLE, date.visible)
        writeString(Date.FORMAT, date.format)

        writeBoolean(Animation.ENABLED, animation.enabled)
        writeInt(Complication.COLOR, complication.color)
    }

    companion object {
        fun read(readInt: (String) -> Int,
                 readBoolean: (String) -> Boolean,
                 readString: (String) -> String) = Configuration(
                Second(readInt(Second.COLOR)),
                Minute(readInt(Minute.COLOR)),
                Hour(readInt(Hour.COLOR)),
                Digital(readInt(Digital.COLOR), readBoolean(Digital.VISIBLE), readBoolean(Digital.IS24)),
                Date(readInt(Date.COLOR), readBoolean(Date.VISIBLE), readString(Date.FORMAT)),
                Complication(readInt(Complication.COLOR)),
                Animation(readBoolean(Animation.ENABLED))
        )
    }
}

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
    }
}

@Parcelize
data class Animation(var enabled: Boolean) : Parcelable {
    companion object {
        const val ENABLED = "animation.enabled"
    }
}
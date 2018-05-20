package com.markantoni.linies.configuration

import android.content.Context
import android.os.Bundle
import com.markantoni.linies.ui.watch.drawers.DrawerType

fun Bundle.withConfiguration(context: Context, setup: Configuration.() -> Unit) {
    fun Int.write(key: String) = putInt(key, this)
    fun Boolean.write(key: String) = putBoolean(key, this)
    fun String.write(key: String) = putString(key, this)

    Preferences.configuration(context).apply {
        setup()
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
}

fun Bundle.getConfiguration(): Configuration {
    fun String.readInt() = getInt(this)
    fun String.readBoolean() = getBoolean(this)
    fun String.readString() = getString(this)

    return Configuration(
            Second(Second.COLOR.readInt()),
            Minute(Minute.COLOR.readInt()),
            Hour(Hour.COLOR.readInt()),
            Digital(Digital.COLOR.readInt(), Digital.VISIBLE.readBoolean(), Digital.IS24.readBoolean()),
            Date(Date.COLOR.readInt(), Date.VISIBLE.readBoolean(), Date.FORMAT.readString()),
            Complication(Complication.COLOR.readInt()),
            Animation(Animation.ENABLED.readBoolean())
    )
}

fun Configuration.findHand(type: DrawerType): Hand = when (type) {
    DrawerType.SECOND -> second
    DrawerType.MINUTE -> minute
    DrawerType.HOUR -> hour
    DrawerType.DIGITAL -> digital
    DrawerType.DATE -> date
    DrawerType.COMPLICATION -> complication
}

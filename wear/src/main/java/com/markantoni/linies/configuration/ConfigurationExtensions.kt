package com.markantoni.linies.configuration

import android.content.Context
import android.os.Bundle
import com.markantoni.linies.ui.watch.drawers.DrawerType

fun Bundle.withConfiguration(context: Context, setup: Configuration.() -> Unit) {
    Preferences.configuration(context)
            .also { it.setup() }
            .write(::putInt, ::putBoolean, ::putString)
}

fun Bundle.getConfiguration() = Configuration.read(::getInt, ::getBoolean, ::getString)

fun Configuration.findHand(type: DrawerType): Hand = when (type) {
    DrawerType.SECOND -> second
    DrawerType.MINUTE -> minute
    DrawerType.HOUR -> hour
    DrawerType.DIGITAL -> digital
    DrawerType.DATE -> date
    DrawerType.COMPLICATION -> complication
}

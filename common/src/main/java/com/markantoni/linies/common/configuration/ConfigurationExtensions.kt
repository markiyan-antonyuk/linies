package com.markantoni.linies.common.configuration

import android.os.Bundle
import com.markantoni.linies.common.drawers.DrawerType


fun Bundle.putConfiguration(configuration: Configuration) {
    configuration.write(::putInt, { s, v -> putBoolean(s, v) }, ::putString)
}

fun Bundle.getConfiguration() = Configuration.read(::getInt, { getBoolean(it) }, ::getString)

fun Configuration.findHand(type: DrawerType): Hand = when (type) {
    DrawerType.SECOND -> second
    DrawerType.MINUTE -> minute
    DrawerType.HOUR -> hour
    DrawerType.DIGITAL -> digital
    DrawerType.DATE -> date
    DrawerType.COMPLICATION -> complication
}
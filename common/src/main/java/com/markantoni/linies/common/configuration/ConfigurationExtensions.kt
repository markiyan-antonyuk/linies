package com.markantoni.linies.common.configuration

import android.os.Bundle
import com.markantoni.linies.common.drawers.DrawerType


//fixme
fun Bundle.putConfiguration(configuration: Configuration) {
    configuration.write(::putInt, ::putBoolean, ::putString)
}

//fixme
fun Bundle.getConfiguration() = Configuration.read(::getInt, ::getBoolean, ::getString)

fun Configuration.findHand(type: DrawerType): Hand = when (type) {
    DrawerType.SECOND -> second
    DrawerType.MINUTE -> minute
    DrawerType.HOUR -> hour
    DrawerType.DIGITAL -> digital
    DrawerType.DATE -> date
    DrawerType.COMPLICATION -> complication
}

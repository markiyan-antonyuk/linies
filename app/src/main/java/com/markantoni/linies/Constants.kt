package com.markantoni.linies

import android.support.wearable.complications.ComplicationData

object Key {
    const val TYPE = "key.type"
    const val COLOR = "key.color"
    const val VISIBLE = "key.visible"
}

object Type {
    const val UNKNOWN = -1
    const val HOUR = 0
    const val MINUTE = 1
    const val SECOND = 2
    const val DIGITAL = 3
    const val DATE = 4
}

object Complication {
    const val LEFT = 0
    const val RIGHT = 1

    val IDS = intArrayOf(LEFT, RIGHT)
    val SUPPORTED_TYPES = intArrayOf(ComplicationData.TYPE_ICON, ComplicationData.TYPE_SHORT_TEXT, ComplicationData.TYPE_SMALL_IMAGE)
}
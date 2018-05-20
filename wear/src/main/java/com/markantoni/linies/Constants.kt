package com.markantoni.linies

import android.support.wearable.complications.ComplicationData

@Deprecated("Soon to be removed")
object Key {
    const val TYPE = "key.type"
    const val COLOR = "key.color"
    const val VISIBLE = "key.visible"
    const val HOURS24 = "key.hours24"
    const val DATE_FORMAT = "key.date.format"
    const val ANIMATING = "key.animate"
}

@Deprecated("Soon to be removed")
object Type {
    const val UNKNOWN = -1
    const val HOUR = 0
    const val MINUTE = 1
    const val SECOND = 2
    const val DIGITAL = 3
    const val DATE = 4
    const val COMPLICATIONS = 5
}

@Deprecated("Soon to be removed")
object Complications {
    const val CENTER = 0
    const val BOTTOM = 1

    val IDS = intArrayOf(CENTER, BOTTOM)
    val SUPPORTED_TYPES = mapOf(
            CENTER to intArrayOf(ComplicationData.TYPE_ICON, ComplicationData.TYPE_LONG_TEXT, ComplicationData.TYPE_RANGED_VALUE),
            BOTTOM to intArrayOf(ComplicationData.TYPE_SHORT_TEXT, ComplicationData.TYPE_RANGED_VALUE)
    )
}
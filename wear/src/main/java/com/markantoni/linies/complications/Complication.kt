package com.markantoni.linies.complications

import android.support.wearable.complications.ComplicationData

object Complication {
    const val CENTER = 0
    const val BOTTOM = 1

    val IDS = intArrayOf(CENTER, BOTTOM)
    val SUPPORTED_TYPES = mapOf(
            CENTER to intArrayOf(ComplicationData.TYPE_ICON, ComplicationData.TYPE_LONG_TEXT, ComplicationData.TYPE_RANGED_VALUE),
            BOTTOM to intArrayOf(ComplicationData.TYPE_SHORT_TEXT, ComplicationData.TYPE_RANGED_VALUE)
    )
}
package com.markantoni.linies.complications

import android.support.wearable.complications.ComplicationData
import com.markantoni.linies.common.configuration.Complication.Companion.BOTTOM
import com.markantoni.linies.common.configuration.Complication.Companion.CENTER

object Complication {
    val IDS = intArrayOf(CENTER, BOTTOM)
    val SUPPORTED_TYPES = mapOf(
            CENTER to intArrayOf(ComplicationData.TYPE_ICON, ComplicationData.TYPE_LONG_TEXT, ComplicationData.TYPE_RANGED_VALUE),
            BOTTOM to intArrayOf(ComplicationData.TYPE_SHORT_TEXT, ComplicationData.TYPE_RANGED_VALUE)
    )
}
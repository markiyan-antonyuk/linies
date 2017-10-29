package com.markantoni.linies.ui.watch.drawers

import android.content.Context
import android.graphics.Canvas
import com.markantoni.linies.Type
import com.markantoni.linies.preference.PreferenceHelper
import java.util.*

object Drawers {
    fun createDrawers(context: Context) = mutableListOf(
            createSecondsDrawer(context), createMinutesDrawer(context), createHoursDrawer(context),
            createDigitalDrawer(context), createDateDrawer(context)
    )

    private fun createSecondsDrawer(context: Context) = object : SectorDrawer(Type.SECOND, PreferenceHelper.getColor(context, Type.SECOND), 60, 95f, 92f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.SECOND)

        override fun onDraw(canvas: Canvas, calendar: Calendar) {
            if (!isAmbientMode) super.onDraw(canvas, calendar)
        }
    }

    private fun createMinutesDrawer(context: Context) = object : SectorDrawer(Type.MINUTE, PreferenceHelper.getColor(context, Type.MINUTE), 60, 90f, 85f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.MINUTE)
    }

    private fun createHoursDrawer(context: Context) = object : SectorDrawer(Type.HOUR, PreferenceHelper.getColor(context, Type.HOUR), 12, 80f, 75f, 6f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.HOUR)
    }

    private fun createDigitalDrawer(context: Context) = DigitalDrawer(PreferenceHelper.getColor(context, Type.DIGITAL), 3f, PreferenceHelper.isVisible(context, Type.DIGITAL),
            PreferenceHelper.is24Hours(context, Type.DIGITAL), 2.2f, 0f)

    private fun createDateDrawer(context: Context) = DateDrawer(PreferenceHelper.getColor(context, Type.DATE), 2f, PreferenceHelper.isVisible(context, Type.DIGITAL),
            PreferenceHelper.getDateFormat(context, Type.DATE), 8f, -3.75f)
}

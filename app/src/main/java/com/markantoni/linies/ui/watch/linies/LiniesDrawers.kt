package com.markantoni.linies.ui.watch.linies

import android.content.Context
import android.graphics.Canvas
import com.markantoni.linies.Type
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.watch.drawers.DateWatchDrawer
import com.markantoni.linies.ui.watch.drawers.DigitalWatchDrawer
import com.markantoni.linies.ui.watch.drawers.WatchHandDrawer
import java.util.*

object LiniesDrawers {
    fun createDrawers(context: Context) = mutableListOf(
            createSecondsDrawer(context), createMinutesDrawer(context), createHoursDrawer(context),
            createDigitalDrawer(context), createDateDrawer(context)
    )

    private fun createSecondsDrawer(context: Context) = object : WatchHandDrawer(Type.SECOND, PreferenceHelper.getColor(context, Type.SECOND), 60, 95f, 92f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.SECOND)

        override fun draw(canvas: Canvas, calendar: Calendar) {
            if (paint.isAntiAlias) super.draw(canvas, calendar)
        }
    }

    private fun createMinutesDrawer(context: Context) = object : WatchHandDrawer(Type.MINUTE, PreferenceHelper.getColor(context, Type.MINUTE), 60, 90f, 85f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.MINUTE)
    }

    private fun createHoursDrawer(context: Context) = object : WatchHandDrawer(Type.HOUR, PreferenceHelper.getColor(context, Type.HOUR), 12, 80f, 75f, 6f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.HOUR)
    }

    private fun createDigitalDrawer(context: Context) = DigitalWatchDrawer(PreferenceHelper.getColor(context, Type.DIGITAL), 3f, PreferenceHelper.isVisible(context, Type.DIGITAL), 2.5f, 5f)

    private fun createDateDrawer(context: Context) = DateWatchDrawer(PreferenceHelper.getColor(context, Type.DATE), 2f, PreferenceHelper.isVisible(context, Type.DIGITAL), 8f, 2f, "dd-MM-YYYY")
}

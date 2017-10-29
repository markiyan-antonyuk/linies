package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import com.markantoni.linies.Type
import com.markantoni.linies.preference.Preferences
import java.util.*

object Drawers {
    fun createDrawers(preferences: Preferences) = mutableListOf(
            createSecondsDrawer(preferences), createMinutesDrawer(preferences), createHoursDrawer(preferences),
            createDigitalDrawer(preferences), createDateDrawer(preferences)
    )

    private fun createSecondsDrawer(preferences: Preferences) = object : SectorDrawer(Type.SECOND, preferences.getColor(Type.SECOND), 60, 95f, 92f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.SECOND)

        override fun onDraw(canvas: Canvas, calendar: Calendar) {
            if (!isAmbientMode) super.onDraw(canvas, calendar)
        }
    }

    private fun createMinutesDrawer(preferences: Preferences) = object : SectorDrawer(Type.MINUTE, preferences.getColor(Type.MINUTE), 60, 90f, 85f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.MINUTE)
    }

    private fun createHoursDrawer(preferences: Preferences) = object : SectorDrawer(Type.HOUR, preferences.getColor(Type.HOUR), 12, 80f, 75f, 6f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.HOUR)
    }

    private fun createDigitalDrawer(preferences: Preferences) = DigitalDrawer(preferences.getColor(Type.DIGITAL), 3f, preferences.isVisible(Type.DIGITAL),
            preferences.is24Hours(), 2.2f, 0f)

    private fun createDateDrawer(preferences: Preferences) = DateDrawer(preferences.getColor(Type.DATE), 2f, preferences.isVisible(Type.DIGITAL),
            preferences.getDateFormat(), 8f, -3.75f)
}

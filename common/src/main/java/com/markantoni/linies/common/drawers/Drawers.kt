package com.markantoni.linies.common.drawers

import android.graphics.Canvas
import com.markantoni.linies.common.configuration.Configuration
import java.util.*

object Drawers {
    fun createDrawers(configuration: Configuration) = mutableListOf(
            createSecondsDrawer(configuration), createMinutesDrawer(configuration), createHoursDrawer(configuration),
            createDigitalDrawer(configuration), createDateDrawer(configuration)
    )

    private fun createSecondsDrawer(configuration: Configuration) = object : SectorDrawer(
            configuration.second.color, Type.SECOND,
            configuration.animation.enabled,
            60, 95f, 92f) {

        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.SECOND)

        override fun onDraw(canvas: Canvas, calendar: Calendar) {
            if (!isAmbientMode) super.onDraw(canvas, calendar)
        }
    }

    private fun createMinutesDrawer(configuration: Configuration) = object : SectorDrawer(
            configuration.minute.color, Type.MINUTE,
            configuration.animation.enabled,
            60, 90f, 85f) {

        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.MINUTE)
    }

    private fun createHoursDrawer(configuration: Configuration) = object : SectorDrawer(
            configuration.hour.color, Type.HOUR,
            configuration.animation.enabled,
            12, 80f, 75f, 6f) {
        override fun calculateSector(calendar: Calendar) = calendar.get(Calendar.HOUR)
    }

    private fun createDigitalDrawer(configuration: Configuration) = DigitalDrawer(
            configuration.digital.color,
            3f, configuration.digital.visible, configuration.digital.is24,
            2.2f, 0f)

    private fun createDateDrawer(configuration: Configuration) = DateDrawer(
            configuration.date.color, 2f, configuration.digital.visible,
            configuration.date.format, 8f, -3.75f)
}

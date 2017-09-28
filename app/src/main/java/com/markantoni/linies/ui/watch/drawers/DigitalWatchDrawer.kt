package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.graphics.Rect
import com.markantoni.linies.Type
import java.text.SimpleDateFormat
import java.util.*

class DigitalWatchDrawer(color: Int, strokeWidth: Float,
                         private var visible: Boolean,
                         private val textSizeCoefficient: Float) : WatchDrawer(Type.DIGITAL, color, strokeWidth) {

    private val bounds = Rect()
    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun updateSize(radius: Float, circleLength: Float) {
        paint.textSize = radius / textSizeCoefficient
    }

    override fun setAmbientMode(ambient: Boolean) {
        paint.isAntiAlias = !ambient
    }

    override fun updateConfiguration(color: Int, visible: Boolean) {
        paint.color = color
        this.visible = visible
    }

    override fun draw(canvas: Canvas, calendar: Calendar) {
        if (!visible) return

        val digitalTime = formatter.format(calendar.time)
        paint.getTextBounds(digitalTime, 0, digitalTime.length, bounds)
        canvas.drawText(digitalTime, -bounds.centerX().toFloat(), -bounds.centerY().toFloat(), paint)
    }
}
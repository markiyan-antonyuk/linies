package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.graphics.Rect
import com.markantoni.linies.Type
import java.text.SimpleDateFormat
import java.util.*

class DateWatchDrawer(color: Int, strokeWidth: Float,
                      private var visible: Boolean,
                      private val textSizeCoefficient: Float,
                      private val yCoefficient: Float, formatPattern: String) : WatchDrawer(Type.DATE, color, strokeWidth) {

    private val bounds = Rect()
    private var formatter: SimpleDateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
    private var radius = 0f

    override fun updateSize(radius: Float, circleLength: Float) {
        this.radius = radius
        paint.textSize = radius / textSizeCoefficient
    }

    override fun setAmbientMode(ambient: Boolean) {
        paint.isAntiAlias = !ambient
    }

    override fun updateConfiguration(color: Int, visible: Boolean, hours24: Boolean) {
        paint.color = color
        this.visible = visible
    }

    override fun draw(canvas: Canvas, calendar: Calendar) {
        if (!visible) return

        val date = formatter.format(calendar.time)
        paint.getTextBounds(date, 0, date.length, bounds)

        canvas.apply {
            save()
            canvas.translate(0f, radius / yCoefficient)
            canvas.drawText(date, -bounds.centerX().toFloat(), -bounds.centerY().toFloat(), paint)
            restore()
        }
    }
}
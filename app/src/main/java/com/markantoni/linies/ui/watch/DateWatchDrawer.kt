package com.markantoni.linies.ui.watch

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import java.text.SimpleDateFormat
import java.util.*

class DateWatchDrawer(private val color: Int, private var visible: Boolean,
                      private val strokeWidth: Float, private val textSizeCoefficient: Float,
                      private val yCoefficient: Float, formatPattern: String) : WatchDrawer {

    private val paint = Paint()
    private val bounds = Rect()
    private var formatter: SimpleDateFormat //todo format configuration
    private var radius = 0f

    init {
        paint.apply {
            strokeWidth = this@DateWatchDrawer.strokeWidth
            color = this@DateWatchDrawer.color
            isAntiAlias = true
        }
        formatter = SimpleDateFormat(formatPattern, Locale.getDefault())
    }

    override fun updateSize(radius: Float, circleLength: Float) {
        this.radius = radius
        paint.textSize = radius / textSizeCoefficient
    }

    override fun setAmbientMode(ambient: Boolean) {
        paint.isAntiAlias = !ambient
    }

    override fun updateConfiguration(color: Int, visible: Boolean) {
        paint.color = color
        this.visible = visible
    }

    fun draw(canvas: Canvas, date: Date) {
        if (!visible) return

        val date = formatter.format(date)
        paint.getTextBounds(date, 0, date.length, bounds)

        canvas.apply {
            save()
            canvas.translate(0f, radius / yCoefficient)
            canvas.drawText(date, -bounds.centerX().toFloat(), -bounds.centerY().toFloat(), paint)
            restore()
        }
    }
}
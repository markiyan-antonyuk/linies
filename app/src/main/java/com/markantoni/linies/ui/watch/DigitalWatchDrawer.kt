package com.markantoni.linies.ui.watch

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import java.text.SimpleDateFormat
import java.util.*

class DigitalWatchDrawer(private val color: Int, private var visible: Boolean,
                         private val strokeWidth: Float, private val textSizeCoefficient: Float) : WatchDrawer {

    private val paint = Paint()
    private val bounds = Rect()
    private val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    init {
        paint.apply {
            strokeWidth = this@DigitalWatchDrawer.strokeWidth
            color = this@DigitalWatchDrawer.color
            isAntiAlias = true
        }
    }

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

    fun draw(canvas: Canvas, date: Date) {
        if (!visible) return

        val digitalTime = formatter.format(date)
        paint.getTextBounds(digitalTime, 0, digitalTime.length, bounds)
        canvas.drawText(digitalTime, -bounds.centerX().toFloat(), -bounds.centerY().toFloat(), paint)
    }
}
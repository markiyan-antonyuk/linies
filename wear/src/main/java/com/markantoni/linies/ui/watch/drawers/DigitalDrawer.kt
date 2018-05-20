package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.markantoni.linies.Configuration
import java.text.SimpleDateFormat
import java.util.*

class DigitalDrawer(color: Int, strokeWidth: Float,
                    var visible: Boolean,
                    private var is24Hours: Boolean,
                    private val textSizeCoefficient: Float,
                    private val yCoefficient: Float) : Drawer(color, strokeWidth) {

    private val bounds = Rect()
    private lateinit var formatter: SimpleDateFormat
    private var radius = 0f

    private val amPmFormatter = SimpleDateFormat("a", Locale.getDefault())
    private val amPmPaint = Paint()
    private var amPmMargin = 0f

    init {
        updateFormatter()
        paint.textScaleX = 0.85f
        amPmPaint.apply {
            this.color = color
            this.strokeWidth = strokeWidth / 2
            isAntiAlias = true
        }
    }

    override fun updateSize(radius: Float, circleLength: Float) {
        this.radius = radius
        paint.textSize = radius / textSizeCoefficient
        amPmPaint.textSize = paint.textSize / 3.5f
        amPmMargin = radius * 0.01f
    }

    override fun updateAmbientMode(ambient: Boolean) {
        paint.isAntiAlias = !ambient
        amPmPaint.isAntiAlias = !ambient
    }

    override fun updateConfiguration(configuration: Configuration) {
        configuration.digital.apply {
            paint.color = color
            amPmPaint.color = color
            this@DigitalDrawer.visible = visible
            is24Hours = is24
        }
    }

    override fun onDraw(canvas: Canvas, calendar: Calendar) {
        if (!visible) return

        val digitalTime = formatter.format(calendar.time)
        paint.getTextBounds(digitalTime, 0, digitalTime.length, bounds)
        canvas.apply {
            save()
            if (yCoefficient != 0f) canvas.translate(0f, radius / yCoefficient)
            canvas.drawText(digitalTime, -bounds.centerX().toFloat(), -bounds.centerY().toFloat(), paint)
            if (!is24Hours) {
                val marker = amPmFormatter.format(calendar.time)
                canvas.drawText(marker, bounds.centerX().toFloat() + amPmMargin, -bounds.centerY().toFloat(), amPmPaint)
            }
            restore()
        }
    }

    private fun updateFormatter() {
        formatter = SimpleDateFormat("${if (is24Hours) "HH" else "hh"}:mm", Locale.getDefault())
    }
}
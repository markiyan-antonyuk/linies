package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import com.markantoni.linies.Key
import com.markantoni.linies.Type
import com.markantoni.linies.preference.Preferences
import java.text.SimpleDateFormat
import java.util.*

class DateDrawer(color: Int, strokeWidth: Float,
                 private var visible: Boolean,
                 formatPattern: String,
                 private val textSizeCoefficient: Float,
                 private val yCoefficient: Float) : Drawer(Type.DATE, color, strokeWidth) {

    private val bounds = Rect()
    private lateinit var formatter: SimpleDateFormat
    private var radius = 0f

    init {
        updateFormatter(formatPattern)
    }

    override fun updateSize(radius: Float, circleLength: Float) {
        this.radius = radius
        paint.textSize = radius / textSizeCoefficient
    }

    override fun updateAmbientMode(ambient: Boolean) {
        paint.isAntiAlias = !ambient
    }

    override fun updateConfiguration(bundle: Bundle, preferences: Preferences) {
        if (bundle.containsKey(Key.COLOR)) {
            val color = bundle.getInt(Key.COLOR)
            paint.color = color
            preferences.setColor(type, color)
        }

        if (bundle.containsKey(Key.VISIBLE)) {
            val visible = bundle.getBoolean(Key.VISIBLE)
            this.visible = visible
            preferences.setVisible(type, visible)
        }

        if (bundle.containsKey(Key.DATE_FORMAT)) {
            val dateFormat = bundle.getString(Key.DATE_FORMAT)
            updateFormatter(dateFormat)
            preferences.setDateFormat(dateFormat)
        }
    }

    override fun onDraw(canvas: Canvas, calendar: Calendar) {
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

    private fun updateFormatter(formatPattern: String) {
        formatter = SimpleDateFormat(formatPattern, Locale.getDefault())
    }
}
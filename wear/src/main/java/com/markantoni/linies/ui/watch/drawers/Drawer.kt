package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import com.markantoni.linies.preference.Preferences
import java.util.*

abstract class Drawer(val type: Int, val color: Int, private val strokeWidth: Float) {
    protected val paint = Paint()
    var isJustShown = true
    var isAmbientMode: Boolean = false
        set(value) {
            field = value
            updateAmbientMode(value)
        }

    init {
        paint.apply {
            color = this@Drawer.color
            strokeWidth = this@Drawer.strokeWidth
            isAntiAlias = true
        }
    }

    fun draw(canvas: Canvas, calendar: Calendar) {
        onDraw(canvas, calendar)
        isJustShown = false
    }

    abstract fun updateSize(radius: Float, circleLength: Float)
    abstract fun updateConfiguration(bundle: Bundle, preferences: Preferences)
    protected abstract fun updateAmbientMode(ambient: Boolean)
    protected abstract fun onDraw(canvas: Canvas, calendar: Calendar)
}
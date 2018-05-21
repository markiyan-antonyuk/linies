package com.markantoni.linies.common.drawers

import android.graphics.Canvas
import android.graphics.Paint
import com.markantoni.linies.common.configuration.Configuration
import java.util.*

enum class DrawerType {
    SECOND, MINUTE, HOUR, DIGITAL, DATE, COMPLICATION
}

abstract class Drawer(val color: Int, private val strokeWidth: Float) {
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
    abstract fun updateConfiguration(configuration: Configuration)
    protected abstract fun updateAmbientMode(ambient: Boolean)
    protected abstract fun onDraw(canvas: Canvas, calendar: Calendar)
}
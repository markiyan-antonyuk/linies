package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.graphics.Paint
import java.util.*

abstract class WatchDrawer(val type: Int, val color: Int, private val strokeWidth: Float) {
    protected val paint = Paint()

    init {
        paint.apply {
            color = this@WatchDrawer.color
            strokeWidth = this@WatchDrawer.strokeWidth
            isAntiAlias = true
        }
    }


    abstract fun updateSize(radius: Float, circleLength: Float)
    abstract fun setAmbientMode(ambient: Boolean)
    abstract fun updateConfiguration(color: Int, visible: Boolean)
    abstract fun draw(canvas: Canvas, calendar: Calendar)
}
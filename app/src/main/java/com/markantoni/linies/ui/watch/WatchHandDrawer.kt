package com.markantoni.linies.ui.watch

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.markantoni.linies.util.calculatePercentage

open class WatchHandDrawer(private val color: Int, private val sectors: Int,
                           private val topPercentage: Float, private val bottomPercentage: Float,
                           private val widthCoefficient: Float = 2f) : WatchDrawer {
    private val MAX_ROTATION = 360
    private val DRAWING_RADIUS_X = 90f
    private val DRAWING_RADIUS_Y = 90f

    private val paint = Paint()
    private val drawingRect = RectF()

    init {
        paint.apply {
            color = this@WatchHandDrawer.color
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    override fun setAmbientMode(ambient: Boolean) {
        paint.apply {
            style = if (ambient) Paint.Style.STROKE else Paint.Style.FILL
            isAntiAlias = !ambient
        }
    }

    override fun updateSize(radius: Float, circleLength: Float) {
        val rectWidth = circleLength / (sectors * widthCoefficient)
        val rectTop = calculatePercentage(topPercentage, radius)
        val rectBottom = calculatePercentage(bottomPercentage, radius)
        drawingRect.set(-rectWidth / 2, rectTop, rectWidth / 2, rectBottom)
    }

    override fun updateConfiguration(color: Int, visible: Boolean) {
        paint.color = color
    }

    fun draw(canvas: Canvas, sector: Int) {
        val sector = if (sector == 0) sectors else sector
        (1..sector).forEach {
            val rotation = it * MAX_ROTATION / sectors.toFloat()
            canvas.apply {
                rotate(rotation)
                drawRoundRect(RectF(drawingRect), DRAWING_RADIUS_X, DRAWING_RADIUS_Y, paint)
                rotate(-rotation)
            }
        }
    }
}
package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.markantoni.linies.util.calculatePercentage
import java.util.*

abstract class WatchHandDrawer(type: Int, color: Int, private val sectors: Int,
                               private val topPercentage: Float, private val bottomPercentage: Float,
                               private val widthCoefficient: Float = 2f) : WatchDrawer(type, color, 1f) {

    private val MAX_ROTATION = 360
    private val DRAWING_RADIUS_X = 90f
    private val DRAWING_RADIUS_Y = 90f

    private val drawingRect = RectF()

    init {
        paint.style = Paint.Style.FILL
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

    override fun updateConfiguration(color: Int, visible: Boolean, hours24: Boolean) {
        paint.color = color
    }

    protected abstract fun calculateSector(calendar: Calendar): Int

    override fun draw(canvas: Canvas, calendar: Calendar) {
        val calculatedSector = calculateSector(calendar)
        val sector = if (calculatedSector == 0) sectors else calculatedSector
        (1..sector).forEach {
            val rotation = it * MAX_ROTATION / sectors.toFloat()
            canvas.apply {
                save()
                rotate(rotation + 180f)
                drawRoundRect(RectF(drawingRect), DRAWING_RADIUS_X, DRAWING_RADIUS_Y, paint)
                restore()
            }
        }
    }
}
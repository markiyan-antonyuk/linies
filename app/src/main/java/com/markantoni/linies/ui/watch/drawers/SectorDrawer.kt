package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.markantoni.linies.util.calculatePercentage
import com.markantoni.linies.util.calculatePercentageOf
import com.markantoni.linies.util.scale
import java.util.*

abstract class SectorDrawer(type: Int, color: Int, private val sectors: Int,
                            private val topPercentage: Float, private val bottomPercentage: Float,
                            private val widthCoefficient: Float = 2f) : WatchDrawer(type, color, 1f) {

    private val ANIMATION_DURATION = 350L
    private val MAX_ROTATION = 360
    private val DRAWING_RADIUS_X = 90f
    private val DRAWING_RADIUS_Y = 90f

    private val initialRect = RectF()
    private val drawingRect = RectF()
    private var lastSector = -1

    private var animationStartTime = 0L
    private var isAnimationRunning = false

    init {
        paint.style = Paint.Style.FILL
    }

    override fun updateAmbientMode(ambient: Boolean) {
        paint.apply {
            style = if (ambient) Paint.Style.STROKE else Paint.Style.FILL
            isAntiAlias = !ambient
        }
    }

    override fun updateSize(radius: Float, circleLength: Float) {
        val rectWidth = circleLength / (sectors * widthCoefficient)
        val rectTop = calculatePercentage(topPercentage, radius)
        val rectBottom = calculatePercentage(bottomPercentage, radius)
        initialRect.set(-rectWidth / 2, rectTop, rectWidth / 2, rectBottom)
        drawingRect.set(initialRect)
    }

    override fun updateConfiguration(color: Int, visible: Boolean, hours24: Boolean, dateFormat: String) {
        paint.color = color
    }

    protected abstract fun calculateSector(calendar: Calendar): Int

    override fun onDraw(canvas: Canvas, calendar: Calendar) {
        val now = System.currentTimeMillis()
        val calculatedSector = calculateSector(calendar)
        val sector = if (calculatedSector == 0) sectors else calculatedSector
        if (sector != lastSector && !isJustShown && !isAmbientMode) {
            isAnimationRunning = true
            animationStartTime = now
            lastSector = sector
        }

        val animationElapsed = now - animationStartTime
        if (isAnimationRunning && animationElapsed > ANIMATION_DURATION) isAnimationRunning = false

        drawSectors(canvas, sector, animationElapsed)
        if (sector == 1) drawSectorsHiding(canvas, animationElapsed)
    }

    private fun drawSectors(canvas: Canvas, sector: Int, animationElapsed: Long) {
        (1..sector).forEach {
            val rotation = calculateRotation(it)
            canvas.apply {
                save()
                drawingRect.set(initialRect)
                rotate(rotation)

                if (it == sector && isAnimationRunning) {
                    val animationStep = 100 - calculatePercentageOf(animationElapsed.toFloat(), ANIMATION_DURATION.toFloat())
                    val rotationStep = -calculatePercentage(animationStep, MAX_ROTATION / sectors.toFloat())
                    canvas.rotate(rotationStep)
                }

                drawRoundRect(drawingRect, DRAWING_RADIUS_X, DRAWING_RADIUS_Y, paint)
                restore()
            }
        }
    }

    private fun drawSectorsHiding(canvas: Canvas, animationElapsed: Long) {
        if (isAnimationRunning) {
            val animationStep = calculatePercentageOf(animationElapsed.toFloat(), ANIMATION_DURATION.toFloat()) / 100f
            drawingRect.apply {
                set(initialRect)
                scale(animationStep)
            }

            (2..sectors).forEach {
                val rotation = calculateRotation(it)
                canvas.apply {
                    save()
                    rotate(rotation)
                    drawRoundRect(drawingRect, DRAWING_RADIUS_X, DRAWING_RADIUS_Y, paint)
                    restore()
                }
            }
        }
    }

    private fun calculateRotation(sector: Int) = sector * MAX_ROTATION / sectors.toFloat() + 180f
}
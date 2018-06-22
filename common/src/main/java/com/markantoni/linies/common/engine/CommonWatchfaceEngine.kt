package com.markantoni.linies.common.engine

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.SurfaceHolder
import com.markantoni.linies.common.configuration.getConfiguration
import com.markantoni.linies.common.drawers.DateDrawer
import com.markantoni.linies.common.drawers.DigitalDrawer
import com.markantoni.linies.common.drawers.Drawer
import com.markantoni.linies.common.drawers.Drawers
import com.markantoni.linies.common.util.Timer
import com.markantoni.linies.common.util.findInstance
import java.util.*
import java.util.concurrent.TimeUnit

abstract class CommonWatchfaceEngine : WatchfaceEngine {

    private val secondsTimer = Timer(TimeUnit.SECONDS) { invalidate() }
    protected val calendar = Calendar.getInstance()
    protected lateinit var drawers: MutableList<Drawer>

    private var isAnimating = false

    override fun onCreate(holder: SurfaceHolder) {
        val configuration = configuration
        drawers = Drawers.createDrawers(configuration)
        isAnimating = configuration.animation.enabled

        if (!isAnimating) secondsTimer.start()
    }

    override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        val radius = (width + height) / 4f
        val length = (radius * 2 * Math.PI).toFloat()

        drawers.forEach { it.updateSize(radius, length) }
    }

    override fun onDestroy() {
        if (!isAnimating) secondsTimer.stop()
    }

    override fun onVisibilityChanged(visible: Boolean) {
        if (!isAnimating) secondsTimer.apply { if (visible) start() else stop() }
        if (visible) {
            drawers.forEach { it.isJustShown = true }
            invalidate()
        }
    }

    override fun onAmbientModeChanged(inAmbientMode: Boolean) {
        drawers.forEach {
            it.isJustShown = true
            it.isAmbientMode = inAmbientMode
        }
        if (!isAnimating) secondsTimer.apply { if (inAmbientMode) stop() else start() }
        invalidate()
    }

    override fun onDraw(canvas: Canvas, bounds: Rect) {
        calendar.timeInMillis = System.currentTimeMillis()

        canvas.apply {
            drawColor(Color.BLACK)
            save()
            translate(bounds.centerX().toFloat(), bounds.centerY().toFloat())
            val digitalDrawerVisible = drawers.findInstance<DigitalDrawer>()?.visible ?: false
            if (isCentralComplicationVisible || !digitalDrawerVisible) {
                val digitalDrawer = drawers.findInstance<DigitalDrawer>()
                val dateDrawer = drawers.findInstance<DateDrawer>()
                drawers.filter { it != digitalDrawer && it != dateDrawer }.forEach { it.draw(canvas, calendar) }
                save()
                translate(0f, -height / 7f)
                digitalDrawer?.draw(canvas, calendar)
                dateDrawer?.draw(canvas, calendar)
                restore()
            } else {
                drawers.forEach { it.draw(canvas, calendar) }
            }
            restore()
        }

        if (isVisible && !isInAmbientMode && isAnimating) invalidate()
    }

    override fun updateConfiguration(bundle: Bundle) {
        bundle.getConfiguration().apply {
            saveConfiguration(this)
            drawers.forEach { it.updateConfiguration(this) }
            isAnimating = animation.enabled
        }
        invalidate()
    }
}
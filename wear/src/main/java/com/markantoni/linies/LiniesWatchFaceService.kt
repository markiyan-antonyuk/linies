package com.markantoni.linies

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.wearable.complications.ComplicationData
import android.support.wearable.watchface.CanvasWatchFaceService
import android.support.wearable.watchface.WatchFaceService
import android.support.wearable.watchface.WatchFaceStyle
import android.view.SurfaceHolder
import com.markantoni.linies.data.getType
import com.markantoni.linies.data.transfer.DataReceiver
import com.markantoni.linies.preference.WatchFacePreferences
import com.markantoni.linies.ui.watch.drawers.ComplicationsDrawer
import com.markantoni.linies.ui.watch.drawers.DigitalDrawer
import com.markantoni.linies.ui.watch.drawers.Drawers
import com.markantoni.linies.util.SecondsTimer
import com.markantoni.linies.util.logd
import java.util.*


class LiniesWatchFaceService : CanvasWatchFaceService() {
    override fun onCreateEngine() = Engine()

    inner class Engine : CanvasWatchFaceService.Engine() {
        private val preferences by lazy { WatchFacePreferences(this@LiniesWatchFaceService) }
        private val calendar = Calendar.getInstance()
        private val timeZoneReceiver = TimeZoneReceiver { updateTimeZone(true) }
        private val dataReceiver by lazy { DataReceiver(this@LiniesWatchFaceService) { updateConfiguration(it) } }

        private var isAnimating = false
        private var ambientMode = false

        private val secondsTimer = SecondsTimer { invalidate() }
        private val drawers by lazy { Drawers.createDrawers(preferences) }
        private lateinit var complicationsDrawer: ComplicationsDrawer

        override fun onCreate(holder: SurfaceHolder) {
            super.onCreate(holder)
            logd("Service started")
            setWatchFaceStyle(WatchFaceStyle.Builder(this@LiniesWatchFaceService)
                    .setCardProgressMode(WatchFaceStyle.PROGRESS_MODE_DISPLAY)
                    .setShowUnreadCountIndicator(true)
                    .setAcceptsTapEvents(true)
                    .build())
            setActiveComplications(*Complications.IDS)

            complicationsDrawer = ComplicationsDrawer(this@LiniesWatchFaceService, preferences.getColor(Type.COMPLICATIONS))
            drawers.add(complicationsDrawer)

            dataReceiver.connect()
            isAnimating = preferences.isAnimating()
            if (!isAnimating) secondsTimer.start()
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            val radius = (width + height) / 4f
            val length = (radius * 2 * Math.PI).toFloat()

            drawers.forEach { it.updateSize(radius, length) }
        }

        override fun onDestroy() {
            if (!isAnimating) secondsTimer.stop()
            dataReceiver.disconnect()
            super.onDestroy()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            updateTimeZone()
            if (!isAnimating) secondsTimer.apply { if (visible) start() else stop() }
            timeZoneReceiver.apply { if (visible) register(this@LiniesWatchFaceService) else unregister(this@LiniesWatchFaceService) }
            if (visible) {
                drawers.forEach { it.isJustShown = true }
                invalidate()
            }
        }

        override fun onAmbientModeChanged(inAmbientMode: Boolean) {
            super.onAmbientModeChanged(inAmbientMode)
            logd("Ambient mode changed: $inAmbientMode")
            ambientMode = inAmbientMode
            drawers.forEach {
                it.isJustShown = true
                it.isAmbientMode = inAmbientMode
            }
            if (!isAnimating) secondsTimer.apply { if (ambientMode) stop() else start() }
            invalidate()
        }

        override fun onComplicationDataUpdate(id: Int, data: ComplicationData) {
            complicationsDrawer.update(id, data)
            invalidate()
        }

        override fun onTapCommand(tapType: Int, x: Int, y: Int, eventTime: Long) {
            if (tapType == WatchFaceService.TAP_TYPE_TAP) complicationsDrawer.handleTap(x, y)
        }

        override fun onDraw(canvas: Canvas, bounds: Rect) {
            super.onDraw(canvas, bounds)
            calendar.timeInMillis = System.currentTimeMillis()

            canvas.apply {
                drawColor(Color.BLACK)
                save()
                translate(bounds.centerX().toFloat(), bounds.centerY().toFloat())
                val digitalDrawerVisible = (drawers.find { it.type == Type.DIGITAL } as? DigitalDrawer)?.visible ?: false
                val complicationDrawer = drawers.find { it.type == Type.COMPLICATIONS } as? ComplicationsDrawer
                if (complicationDrawer != null && complicationDrawer.isComplicationVisible(Complications.CENTER) || !digitalDrawerVisible) {
                    val digitalDrawer = drawers.find { it.type == Type.DIGITAL }
                    val dateDrawer = drawers.find { it.type == Type.DATE }
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

        override fun onTimeTick() {
            invalidate()
        }

        private fun updateTimeZone(invalidate: Boolean = false) {
            calendar.timeZone = TimeZone.getDefault()
            if (invalidate) invalidate()
        }

        private fun updateConfiguration(bundle: Bundle) {
            val type = bundle.getType()
            drawers.find { it.type == type }?.updateConfiguration(bundle, preferences)

            if (bundle.containsKey(Key.ANIMATING)) isAnimating = bundle.getBoolean(Key.ANIMATING)
            invalidate()
        }
    }
}
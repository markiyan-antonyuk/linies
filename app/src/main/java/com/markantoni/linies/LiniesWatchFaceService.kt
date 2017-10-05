package com.markantoni.linies

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.wearable.complications.ComplicationData
import android.support.wearable.watchface.CanvasWatchFaceService
import android.support.wearable.watchface.WatchFaceStyle
import android.view.SurfaceHolder
import com.markantoni.linies.data.transfer.DataReceiver
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.config.complications.ComplicationsDrawer
import com.markantoni.linies.ui.watch.linies.LiniesDrawers
import com.markantoni.linies.util.SecondsTimer
import com.markantoni.linies.util.logd
import java.util.*


class LiniesWatchFaceService : CanvasWatchFaceService() {
    override fun onCreateEngine() = Engine()

    inner class Engine : CanvasWatchFaceService.Engine() {
        private var calendar = Calendar.getInstance()
        private var timeZoneReceiver = TimeZoneReceiver({ updateTimeZone(true) })
        private lateinit var dataReceiver: DataReceiver

        private var ambientMode = false

        private val secondsTimer = SecondsTimer({ invalidate() })
        private val drawers = LiniesDrawers.createDrawers(this@LiniesWatchFaceService)
        private lateinit var complicationsDrawer: ComplicationsDrawer

        override fun onCreate(holder: SurfaceHolder) {
            super.onCreate(holder)
            logd("Service started")
            setWatchFaceStyle(WatchFaceStyle.Builder(this@LiniesWatchFaceService)
                    .setCardProgressMode(WatchFaceStyle.PROGRESS_MODE_DISPLAY)
                    .setShowUnreadCountIndicator(true)
                    .build())
            setActiveComplications(*Complications.IDS)

            complicationsDrawer = ComplicationsDrawer(this@LiniesWatchFaceService, Color.WHITE)
            drawers.add(complicationsDrawer)

            dataReceiver = DataReceiver(this@LiniesWatchFaceService, { onNewData(it) })
            dataReceiver.connect()
            secondsTimer.start()
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            val radius = (width + height) / 4f
            val length = (radius * 2 * Math.PI).toFloat()

            drawers.forEach { it.updateSize(radius, length) }
        }

        override fun onDestroy() {
            secondsTimer.stop()
            dataReceiver.disconnect()
            super.onDestroy()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            updateTimeZone()
            secondsTimer.apply { if (visible) start() else stop() }
            timeZoneReceiver.apply { if (visible) register(this@LiniesWatchFaceService) else unregister(this@LiniesWatchFaceService) }
        }

        override fun onAmbientModeChanged(inAmbientMode: Boolean) {
            super.onAmbientModeChanged(inAmbientMode)
            logd("Ambient mode changed: $inAmbientMode")
            ambientMode = inAmbientMode
            drawers.forEach { it.setAmbientMode(ambientMode) }
            secondsTimer.apply { if (ambientMode) stop() else start() }
        }

        override fun onComplicationDataUpdate(id: Int, data: ComplicationData) = complicationsDrawer.update(id, data)

        override fun onDraw(canvas: Canvas, bounds: Rect) {
            super.onDraw(canvas, bounds)
            calendar.timeInMillis = System.currentTimeMillis()

            canvas.apply {
                drawColor(Color.BLACK)
                save()
                translate(bounds.centerX().toFloat(), bounds.centerY().toFloat())
                drawers.forEach { it.draw(canvas, calendar) }
                restore()
            }
        }

        override fun onTimeTick() {
            super.onTimeTick()
            invalidate()
        }

        private fun updateTimeZone(invalidate: Boolean = false) {
            calendar.timeZone = TimeZone.getDefault()
            if (invalidate) invalidate()
        }

        private fun onNewData(bundle: Bundle) {
            bundle.apply {
                val type = getInt(Key.TYPE, Type.UNKNOWN)
                val color = getInt(Key.COLOR, PreferenceHelper.getColor(this@LiniesWatchFaceService, type))
                val visible = getBoolean(Key.VISIBLE, PreferenceHelper.isVisible(this@LiniesWatchFaceService, type))
                updateDrawerConfiguration(type, color, visible)
            }
        }

        private fun updateDrawerConfiguration(type: Int, color: Int, visible: Boolean) {
            logd("Updating configuration for type: $type")
            PreferenceHelper.update(this@LiniesWatchFaceService, type, color, visible)
            drawers.find { it.type == type }?.updateConfiguration(color, visible)
        }
    }
}
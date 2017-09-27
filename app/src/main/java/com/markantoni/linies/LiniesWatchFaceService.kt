package com.markantoni.linies

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.support.wearable.watchface.CanvasWatchFaceService
import android.support.wearable.watchface.WatchFaceStyle
import android.view.SurfaceHolder
import com.google.android.gms.wearable.DataMap
import com.markantoni.linies.data.transfer.DataReceiver
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.watch.DateWatchDrawer
import com.markantoni.linies.ui.watch.DigitalWatchDrawer
import com.markantoni.linies.ui.watch.WatchHandDrawer
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
        //TODO maybe move it somewhere or extend from base classes, add it also to list and add types
        private val hoursHandDrawer = WatchHandDrawer(PreferenceHelper.getColor(this@LiniesWatchFaceService, Type.HOUR), 12, 80f, 75f, 6f)
        private val minutesHandDrawer = WatchHandDrawer(PreferenceHelper.getColor(this@LiniesWatchFaceService, Type.MINUTE), 60, 90f, 85f)
        private val secondsHandDrawer = WatchHandDrawer(PreferenceHelper.getColor(this@LiniesWatchFaceService, Type.SECOND), 60, 95f, 92f)
        private val digitalDrawer = DigitalWatchDrawer(PreferenceHelper.getColor(this@LiniesWatchFaceService, Type.DIGITAL), PreferenceHelper.isVisible(this@LiniesWatchFaceService, Type.DIGITAL),
                3f, 2.5f)
        private val dateDrawer = DateWatchDrawer(PreferenceHelper.getColor(this@LiniesWatchFaceService, Type.DATE), PreferenceHelper.isVisible(this@LiniesWatchFaceService, Type.DATE),
                2f, 8f, 3.5f, "dd-MM-YYYY")

        override fun onCreate(holder: SurfaceHolder) {
            super.onCreate(holder)
            logd("Service started")
            setWatchFaceStyle(WatchFaceStyle.Builder(this@LiniesWatchFaceService)
                    .setCardProgressMode(WatchFaceStyle.PROGRESS_MODE_DISPLAY)
                    .setShowUnreadCountIndicator(true)
                    .build())

            dataReceiver = DataReceiver(this@LiniesWatchFaceService, { onNewData(it) })
            dataReceiver.connect()

            secondsTimer.start()
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            val radius = (width + height) / 4f
            val length = (radius * 2 * Math.PI).toFloat()

            secondsHandDrawer.updateSize(radius, length)
            minutesHandDrawer.updateSize(radius, length)
            hoursHandDrawer.updateSize(radius, length)
            digitalDrawer.updateSize(radius, length)
            dateDrawer.updateSize(radius, length)
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
            secondsHandDrawer.setAmbientMode(ambientMode)
            minutesHandDrawer.setAmbientMode(ambientMode)
            hoursHandDrawer.setAmbientMode(ambientMode)
            digitalDrawer.setAmbientMode(ambientMode)
            dateDrawer.setAmbientMode(ambientMode)

            secondsTimer.apply { if (ambientMode) stop() else start() }
        }

        override fun onDraw(canvas: Canvas, bounds: Rect) {
            super.onDraw(canvas, bounds)
            calendar.time = Date(System.currentTimeMillis())
            val second = calendar.get(Calendar.SECOND)
            val minute = calendar.get(Calendar.MINUTE)
            val hour = calendar.get(Calendar.HOUR)

            canvas.apply {
                drawColor(Color.BLACK)
                save()
                translate(bounds.centerX().toFloat(), bounds.centerY().toFloat())
                rotate(180f)
                if (!ambientMode) secondsHandDrawer.draw(canvas, second)
                minutesHandDrawer.draw(canvas, minute)
                hoursHandDrawer.draw(canvas, hour)
                rotate(-180f)
                digitalDrawer.draw(canvas, calendar.time)
                dateDrawer.draw(canvas, calendar.time)
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

        private fun onNewData(dataMap: DataMap) {
            val type = dataMap.getInt(Key.TYPE, Type.UNKNOWN)
            val color = dataMap.getInt(Key.COLOR, PreferenceHelper.getColor(this@LiniesWatchFaceService, type))
            val visible = dataMap.getBoolean(Key.VISIBLE, PreferenceHelper.isVisible(this@LiniesWatchFaceService, type))
            updateDrawerConfiguration(type, color, visible)
        }

        private fun updateDrawerConfiguration(type: Int, color: Int, visible: Boolean) {
            logd("Updating configuration for type: $type")
            PreferenceHelper.update(this@LiniesWatchFaceService, type, color, visible)
            when (type) {
                Type.HOUR -> hoursHandDrawer.updateConfiguration(color, visible)
                Type.MINUTE -> minutesHandDrawer.updateConfiguration(color, visible)
                Type.SECOND -> secondsHandDrawer.updateConfiguration(color, visible)
                Type.DIGITAL -> digitalDrawer.updateConfiguration(color, visible)
                Type.DATE -> dateDrawer.updateConfiguration(color, visible)
            }
        }
    }
}
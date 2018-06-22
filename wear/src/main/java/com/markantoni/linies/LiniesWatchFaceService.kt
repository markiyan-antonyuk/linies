package com.markantoni.linies

import android.graphics.Canvas
import android.graphics.Rect
import android.support.wearable.complications.ComplicationData
import android.support.wearable.watchface.CanvasWatchFaceService
import android.view.SurfaceHolder


class LiniesWatchFaceService : CanvasWatchFaceService() {
    override fun onCreateEngine() = Engine()

    inner class Engine : CanvasWatchFaceService.Engine() {
        private val engine = WearableWatchfaceEngine(this@LiniesWatchFaceService, this)

        override fun onCreate(holder: SurfaceHolder) {
            super.onCreate(holder)
            engine.onCreate(holder)
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            engine.onSurfaceChanged(holder, format, width, height)
        }

        override fun onDestroy() {
            engine.onDestroy()
            super.onDestroy()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            engine.onVisibilityChanged(visible)
        }

        override fun onAmbientModeChanged(inAmbientMode: Boolean) {
            super.onAmbientModeChanged(inAmbientMode)
            engine.onAmbientModeChanged(inAmbientMode)
        }

        override fun onComplicationDataUpdate(id: Int, data: ComplicationData) = engine.updateComplicationData(id, data)

        override fun onTapCommand(tapType: Int, x: Int, y: Int, eventTime: Long) = engine.handleTapCommand(tapType, x, y, eventTime)

        override fun onDraw(canvas: Canvas, bounds: Rect) {
            super.onDraw(canvas, bounds)
            engine.onDraw(canvas, bounds)
        }

        override fun onTimeTick() = engine.onTimeTick()
    }
}
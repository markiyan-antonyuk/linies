package com.markantoni.linies.watchface

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class AppWatchfaceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private val engine = AppWatchfaceEngine(this)
    private var drawingThread: DrawingThread? = null

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        engine.onCreate(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        engine.onSurfaceChanged(holder, format, width, height)
        drawingThread?.stop()

        val bounds = Rect(0, 0, width, height)
        drawingThread = DrawingThread {
            val canvas = holder.lockCanvas()
            canvas.drawColor(0, PorterDuff.Mode.CLEAR)
            engine.onDraw(canvas, bounds)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        drawingThread?.stop()
        engine.onDestroy()
    }
}
package com.markantoni.linies.app.watchface

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.markantoni.linies.app.R
import com.markantoni.linies.common.util.radius

class AppWatchfaceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    val engine = AppWatchfaceEngine(this)

    private val framePaint = Paint()
    private val frameSize = resources.getDimensionPixelOffset(R.dimen.watchface_frame_size)

    private var drawingThread: DrawingThread? = null

    init {
        holder.addCallback(this)
        framePaint.apply {
            color = Color.DKGRAY
            style = Paint.Style.STROKE
            strokeWidth = frameSize / 2f
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        engine.onCreate(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        engine.onSurfaceChanged(holder, format, width - frameSize * 2, height - frameSize * 2)
        drawingThread?.stop()

        val bounds = Rect(frameSize, frameSize, width - frameSize, height - frameSize)
        drawingThread = DrawingThread {
            val canvas = holder.lockCanvas()
            canvas.drawColor(0, PorterDuff.Mode.CLEAR)
            engine.onDraw(canvas, bounds)
            canvas.drawWatchFrame()
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun Canvas.drawWatchFrame() = drawCircle(radius, radius, radius - frameSize, framePaint)

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        drawingThread?.stop()
        engine.onDestroy()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = super.onMeasure(widthMeasureSpec, widthMeasureSpec)
}
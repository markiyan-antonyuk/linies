package com.markantoni.linies.common.engine

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.SurfaceHolder

interface WatchfaceEngine {
    fun onCreate(holde: SurfaceHolder)
    fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int)
    fun onDestroy()
    fun onVisibilityChanged(visible: Boolean)
    fun onAmbientModeChanged(inAmbientMode: Boolean)
    fun onDraw(canvas: Canvas, bounds: Rect)
    fun onTimeTick()
    fun updateConfiguration(bundle: Bundle)
    fun invalidate()
}
package com.markantoni.linies.common.engine

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.SurfaceHolder
import com.markantoni.linies.common.configuration.Configuration

interface WatchfaceEngine {
    val isVisible: Boolean
    val isInAmbientMode: Boolean
    val configuration: Configuration
    val isCentralComplicationVisible: Boolean

    //override
    fun onCreate(holder: SurfaceHolder)
    fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int)
    fun onDestroy()
    fun onVisibilityChanged(visible: Boolean)
    fun onAmbientModeChanged(inAmbientMode: Boolean)
    fun onDraw(canvas: Canvas, bounds: Rect)
    fun invalidate()

    //custom
    fun updateConfiguration(bundle: Bundle)
    fun saveConfiguration(configuration: Configuration)

    fun onTimeTick() = invalidate()
}
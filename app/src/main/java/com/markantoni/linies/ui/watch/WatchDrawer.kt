package com.markantoni.linies.ui.watch

interface WatchDrawer {
    fun updateSize(radius: Float, circleLength: Float)
    fun setAmbientMode(ambient: Boolean)
    fun updateConfiguration(color: Int, visible: Boolean)
}
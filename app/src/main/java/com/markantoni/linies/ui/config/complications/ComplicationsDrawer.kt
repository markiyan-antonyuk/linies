package com.markantoni.linies.ui.config.complications

import android.graphics.Canvas
import android.support.wearable.complications.ComplicationData
import android.support.wearable.complications.rendering.ComplicationDrawable
import android.util.SparseArray
import com.markantoni.linies.Complications
import com.markantoni.linies.LiniesWatchFaceService
import com.markantoni.linies.Type
import com.markantoni.linies.ui.watch.drawers.WatchDrawer
import java.util.*

class ComplicationsDrawer(private val service: LiniesWatchFaceService, color: Int) : WatchDrawer(Type.COMPLICATIONS, color, 0f) {
    private val drawables = SparseArray<ComplicationDrawable>(Complications.IDS.size)
    private val complications = SparseArray<ComplicationData>(Complications.IDS.size)

    init {
        Complications.IDS.forEach { id ->
            val drawable = ComplicationDrawable(service).apply {
                init()
                setColor(color)
            }
            drawables.put(id, drawable)
        }
    }

    fun update(id: Int, data: ComplicationData) {
        complications.put(id, data)
        drawables.get(id)?.setComplicationData(data)
    }

    override fun updateConfiguration(color: Int, visible: Boolean) = setColor(color)

    override fun updateSize(radius: Float, circleLength: Float) {
        val height = (radius / 4f).toInt()
        var width = (radius / 2f).toInt()
        var offset = (radius / 2.5f).toInt()
        drawables[Complications.TOP].setBounds(-width / 2, -offset - height, width / 2, -offset)

        width = radius.toInt()
        offset = (radius / 4f).toInt()
        drawables[Complications.BOTTOM].setBounds(-width / 2, offset, width / 2, offset + height)
    }

    override fun setAmbientMode(ambient: Boolean) = Complications.IDS.forEach { drawables[it].setInAmbientMode(ambient) }


    override fun draw(canvas: Canvas, calendar: Calendar) {
        val now = System.currentTimeMillis()
        Complications.IDS.forEach { drawables[it].draw(canvas, now) }
    }

    private fun setColor(color: Int) {
        Complications.IDS.forEach {
            drawables[it].setColor(color)
        }
    }

    private fun ComplicationDrawable.setColor(color: Int) {
        setBorderColorActive(color)
        setBorderColorAmbient(color)

        setTitleColorActive(color)
        setTitleColorAmbient(color)

        setHighlightColorActive(color)
        setHighlightColorAmbient(color)

        setRangedValuePrimaryColorActive(color)
        setRangedValuePrimaryColorAmbient(color)

        setRangedValueSecondaryColorActive(color)
        setRangedValueSecondaryColorAmbient(color)

        setIconColorActive(color)
        setIconColorAmbient(color)
    }

    private fun ComplicationDrawable.init() {
        //todo getDimen
        setBorderDashGapActive(4)
        setBorderDashGapAmbient(4)

        setBorderDashWidthActive(1)
        setBorderDashWidthAmbient(1)

        setBorderRadiusActive(100)
        setBorderRadiusAmbient(100)

        setBorderWidthActive(2)
        setBorderWidthAmbient(2)

        setRangedValueRingWidthActive(2)
        setRangedValueRingWidthAmbient(2)

        setTextColorActive(color)
        setTextColorAmbient(color)

        setTextSizeActive(20)
        setTextSizeAmbient(20)

        setTitleSizeActive(20)
        setTitleSizeAmbient(20)

        setBorderStyleAmbient(ComplicationDrawable.BORDER_STYLE_DASHED)
        setBorderStyleActive(ComplicationDrawable.BORDER_STYLE_DASHED)
    }
}
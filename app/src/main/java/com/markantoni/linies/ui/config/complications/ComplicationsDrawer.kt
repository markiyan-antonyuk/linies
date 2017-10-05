package com.markantoni.linies.ui.config.complications

import android.graphics.Canvas
import android.support.wearable.complications.ComplicationData
import android.support.wearable.complications.rendering.ComplicationDrawable
import android.util.SparseArray
import com.markantoni.linies.Complications
import com.markantoni.linies.LiniesWatchFaceService
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.ui.watch.drawers.WatchDrawer
import java.util.*

class ComplicationsDrawer(private val service: LiniesWatchFaceService, color: Int) : WatchDrawer(Type.COMPLICATIONS, color, 0f) {
    private val drawables = SparseArray<ComplicationDrawable>(Complications.IDS.size)
    private val complications = SparseArray<ComplicationData>(Complications.IDS.size)

    init {
        Complications.IDS.forEach { id ->
            val drawable = service.getDrawable(R.drawable.complication) as ComplicationDrawable
            drawable.setContext(service)
            drawables.put(id, drawable)
        }
        setColor(color)
    }

    fun update(id: Int, data: ComplicationData) {
        complications.put(id, data)
        drawables.get(id)?.setComplicationData(data)
    }

    override fun updateConfiguration(color: Int, visible: Boolean) = setColor(color)

    override fun updateSize(radius: Float, circleLength: Float) {
        val xOffset = (radius / 30).toInt()
        val yOffset = (radius / 12).toInt()
        val width = (radius / 1.75f).toInt()
        val height = (radius / 2.75f).toInt()

        drawables[Complications.LEFT].setBounds(-xOffset - width, -yOffset - height, -xOffset, -yOffset)
        drawables[Complications.RIGHT].setBounds(xOffset, -yOffset - height, xOffset + width, -yOffset)
    }

    override fun setAmbientMode(ambient: Boolean) = Complications.IDS.forEach { drawables[it].setInAmbientMode(ambient) }


    override fun draw(canvas: Canvas, calendar: Calendar) {
        val now = System.currentTimeMillis()
        Complications.IDS.forEach { drawables[it].draw(canvas, now) }
    }

    private fun setColor(color: Int) {
        Complications.IDS.forEach {
            drawables[it].apply {
                setBorderColorActive(color)
                setHighlightColorActive(color)
                setIconColorActive(color)
                setRangedValuePrimaryColorActive(color)
                setRangedValueSecondaryColorActive(color)
                setTextColorActive(color)
                setTitleColorActive(color)
            }
        }
    }
}
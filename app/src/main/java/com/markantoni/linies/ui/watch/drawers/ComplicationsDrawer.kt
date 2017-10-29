package com.markantoni.linies.ui.watch.drawers

import android.graphics.Canvas
import android.support.wearable.complications.ComplicationData
import android.support.wearable.complications.ComplicationHelperActivity
import android.support.wearable.complications.rendering.ComplicationDrawable
import android.util.SparseArray
import com.markantoni.linies.Complications
import com.markantoni.linies.LiniesWatchFaceService
import com.markantoni.linies.Type
import com.markantoni.linies.util.getWatchFaceServiceComponentName
import java.util.*

class ComplicationsDrawer(private val service: LiniesWatchFaceService, color: Int) : Drawer(Type.COMPLICATIONS, color, 0f) {
    private val drawables = SparseArray<ComplicationDrawable>(Complications.IDS.size)
    private val complications = SparseArray<ComplicationData>(Complications.IDS.size)
    private var radius = 0f

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

    override fun updateConfiguration(color: Int, visible: Boolean, hours24: Boolean, dateFormat: String) = setColor(color)

    override fun updateSize(radius: Float, circleLength: Float) {
        this.radius = radius
        var width = (radius / 2f).toInt()
        var height = (radius / 4f).toInt()
        var offset = -(radius / 1.5f).toInt()
        drawables[Complications.BOTTOM].setBounds(-width / 2, -offset - height, width / 2, -offset)

        width = (radius * 1.3f).toInt()
        height = (radius / 3f).toInt()
        offset = 0
        drawables[Complications.CENTER].setBounds(-width / 2, offset, width / 2, offset + height)
    }

    override fun updateAmbientMode(ambient: Boolean) = Complications.IDS.forEach { drawables[it].setInAmbientMode(ambient) }

    override fun onDraw(canvas: Canvas, calendar: Calendar) {
        val now = System.currentTimeMillis()
        Complications.IDS.forEach { drawables[it].draw(canvas, now) }
    }

    fun handleTap(x: Int, y: Int) {
        val x = (x - radius).toInt()
        val y = (y - radius).toInt()

        Complications.IDS.forEach {
            complications[it]?.apply {
                if (isActive(System.currentTimeMillis())
                        && type != ComplicationData.TYPE_NOT_CONFIGURED
                        && type != ComplicationData.TYPE_EMPTY
                        && drawables[it].bounds.contains(x, y)) {
                    if (tapAction != null) {
                        tapAction.send()
                    } else if (type == ComplicationData.TYPE_NO_PERMISSION) {
                        service.apply { startActivity(ComplicationHelperActivity.createPermissionRequestHelperIntent(this, getWatchFaceServiceComponentName())) }
                    }
                }
            }
        }

    }

    fun isComplicationVisible(id: Int): Boolean {
        return complications[id]?.let { complication ->
            complication.type != ComplicationData.TYPE_NOT_CONFIGURED &&
                    complication.type != ComplicationData.TYPE_EMPTY
        } ?: false
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

        setTextColorActive(color)
        setTextColorAmbient(color)
    }

    private fun ComplicationDrawable.init() {
        setRangedValueRingWidthActive(0)
        setRangedValueRingWidthAmbient(0)

        setBorderRadiusActive(5)
        setBorderRadiusAmbient(5)

        setBorderStyleActive(ComplicationDrawable.BORDER_STYLE_NONE)
        setBorderStyleAmbient(ComplicationDrawable.BORDER_STYLE_NONE)
    }
}
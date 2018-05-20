package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import com.markantoni.linies.Key
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.data.setType
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.preference.WatchFacePreferences
import com.markantoni.linies.util.logd
import com.markantoni.linies.util.moveToStart
import kotlinx.android.synthetic.main.activity_radio_group.*

class ColorPickerActivity : Activity() {
    companion object {
        private val EXTRA_TYPE = "extra.type"
        fun newIntent(context: Context, type: Int) = Intent(context, ColorPickerActivity::class.java).apply {
            putExtra(EXTRA_TYPE, type)
        }
    }

    private lateinit var dataSender: DataSender
    private var type = Type.UNKNOWN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio_group)
        type = intent.getIntExtra(EXTRA_TYPE, Type.UNKNOWN)
        dataSender = DataSender(this)
        initColors()
    }

    private fun initColors() {
        val colorNames = resources.getStringArray(R.array.color_names).toMutableList()
        val colors = resources.getStringArray(R.array.color_values).map { Color.parseColor(it) }.toMutableList()
        val currentColor = WatchFacePreferences(this).getColor(type)

        val currentColorIndex = colors.indexOf(currentColor)
        colorNames.moveToStart(currentColorIndex)
        colors.moveToStart(currentColorIndex)

        colorNames.forEachIndexed { index, colorName ->
            (LayoutInflater.from(this).inflate(R.layout.view_color_picker_item, radioGroup, false) as RadioButton).apply {
                val color = colors[index]
                id = View.generateViewId()
                isChecked = color == currentColor
                text = colorName
                setTextColor(color)
                buttonTintList = ColorStateList.valueOf(color)
                setOnCheckedChangeListener { _, checked -> if (checked) sendUpdatedColor(color) }
                radioGroup.addView(this)
            }
        }
    }

    private fun sendUpdatedColor(color: Int) = dataSender.send({
        logd("Sending new color for $type")
        setType(type)
        putInt(Key.COLOR, color)
    })
}
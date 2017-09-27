package com.markantoni.linies.ui

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
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.util.logd
import com.markantoni.linies.util.moveToStart
import kotlinx.android.synthetic.main.activity_color_picker.*

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
        setContentView(R.layout.activity_color_picker)
        type = intent.getIntExtra(EXTRA_TYPE, Type.UNKNOWN)
        dataSender = DataSender(this)
        initColors()
    }

    override fun onStart() {
        super.onStart()
        dataSender.connect()
    }

    override fun onStop() {
        dataSender.disconnect()
        super.onStop()
    }

    private fun initColors() {
        val colorNames = resources.getStringArray(R.array.color_names).toMutableList()
        val colors = resources.getStringArray(R.array.color_values).map { Color.parseColor(it) }.toMutableList()
        val currentColor = PreferenceHelper.getColor(this, type)

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

    private fun sendUpdatedColor(color: Int) {
        logd("Sending new color for $type")
        dataSender.createDataMapRequest().apply {
            dataMap.apply {
                putInt(Key.TYPE, type)
                putInt(Key.COLOR, color)
            }
            dataSender.send(this)
        }
    }
}
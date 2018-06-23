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
import com.markantoni.linies.*
import com.markantoni.linies.preferences.Preferences
import com.markantoni.linies.common.configuration.findHand
import com.markantoni.linies.common.data.DataProtocol
import com.markantoni.linies.common.drawers.DrawerType
import com.markantoni.linies.common.data.DataSender
import com.markantoni.linies.common.util.moveToStart
import com.markantoni.linies.util.withConfiguration
import kotlinx.android.synthetic.main.activity_radio_group.*

class ColorPickerActivity : Activity() {
    companion object {
        private const val EXTRA_TYPE = "extra.type"
        fun newIntent(context: Context, type: DrawerType) = Intent(context, ColorPickerActivity::class.java).apply {
            putExtra(EXTRA_TYPE, type)
        }
    }

    private val dataSender by lazy { DataSender(this) }
    private val type by lazy { intent.getSerializableExtra(EXTRA_TYPE) as DrawerType }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio_group)
        initColors()
    }

    private fun initColors() {
        val colorNames = resources.getStringArray(R.array.color_names).toMutableList()
        val colors = resources.getStringArray(R.array.color_values).map { Color.parseColor(it) }.toMutableList()
        val currentColor = Preferences.configuration(this).findHand(type).color
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

    private fun sendUpdatedColor(color: Int) = dataSender.send(DataProtocol.WEAR) {
        withConfiguration(this@ColorPickerActivity) {
            findHand(type).color = color
        }
    }
}
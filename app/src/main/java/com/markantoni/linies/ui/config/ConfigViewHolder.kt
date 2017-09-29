package com.markantoni.linies.ui.config

import android.content.Context
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.markantoni.linies.data.events.VisiblityChangeEvent
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.ColorPickerActivity
import com.markantoni.linies.util.getCenterOfScreen
import com.markantoni.linies.util.sendEvent
import com.markantoni.linies.util.setVisible
import kotlinx.android.synthetic.main.view_config_item.view.*

class ConfigViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleTv = itemView.titleTv
    private val colorPickerTv = itemView.colorPickerTv
    private val visibleSwitch = itemView.visibleSwitch

    fun bind(configItem: ConfigItem) {
        configItem.apply {
            titleTv.text = title
            colorPickerTv.setOnClickListener { startColorPicker(it.context, type, it) }
            visibleSwitch.apply {
                setOnCheckedChangeListener(null)
                setVisible(configItem.changeVisibility)
                isChecked = PreferenceHelper.isVisible(context, type)
                setOnCheckedChangeListener { _, checked ->
                    sendEvent(VisiblityChangeEvent(type, checked))
                }
            }
        }
    }

    private fun startColorPicker(context: Context, type: Int, view: View) {
        val screenCenter = getCenterOfScreen(context)
        val options = ActivityOptionsCompat.makeClipRevealAnimation(view, screenCenter[0], screenCenter[1], 0, 0).toBundle()
        context.startActivity(ColorPickerActivity.newIntent(context, type), options)
    }
}
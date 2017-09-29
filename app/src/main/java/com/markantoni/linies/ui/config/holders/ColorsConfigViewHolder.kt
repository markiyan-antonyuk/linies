package com.markantoni.linies.ui.config.holders

import android.view.ViewGroup
import android.widget.TextView
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.config.events.OpenColorPickerEvent
import com.markantoni.linies.util.sendEvent
import kotlinx.android.synthetic.main.view_holder_colors_config.view.*

class ColorsConfigViewHolder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.view_holder_colors_config) {
    companion object : HolderType {
        override fun getType() = 0
    }

    init {
        itemView.apply {
            bindToColor(secondsTv, Type.SECOND)
            bindToColor(minutesTv, Type.MINUTE)
            bindToColor(hoursTv, Type.HOUR)
            bindToColor(digitalTv, Type.DIGITAL)
            bindToColor(dateTv, Type.DATE)
        }
    }

    private fun bindToColor(view: TextView, type: Int) = view.apply {
        setOnClickListener { sendEvent(OpenColorPickerEvent(type)) }
        setTextColor(PreferenceHelper.getColor(itemView.context, type))
    }
}
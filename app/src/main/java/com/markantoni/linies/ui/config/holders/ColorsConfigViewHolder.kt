package com.markantoni.linies.ui.config.holders

import android.view.ViewGroup
import android.widget.TextView
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.config.events.OpenColorPickerEvent
import com.markantoni.linies.util.sendEvent
import kotlinx.android.synthetic.main.view_holder_colors_config.view.*

class ColorsConfigViewHolder(parent: ViewGroup) : BaseConfigViewHolder(parent, R.layout.view_holder_colors_config) {
    override fun bind() {
        itemView.apply {
            bind(secondsTv, Type.SECOND)
            bind(minutesTv, Type.MINUTE)
            bind(hoursTv, Type.HOUR)
            bind(digitalTv, Type.DIGITAL)
            bind(dateTv, Type.DATE)
            bind(complicationsTv, Type.COMPLICATIONS)
        }
    }

    private fun bind(view: TextView, type: Int) = view.apply {
        setOnClickListener { sendEvent(OpenColorPickerEvent(type)) }
        setTextColor(PreferenceHelper.getColor(context, type))
    }
}
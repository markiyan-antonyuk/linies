package com.markantoni.linies.ui.config.holders

import android.view.ViewGroup
import android.widget.Switch
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.config.events.VisibilityChangeEvent
import com.markantoni.linies.util.sendEvent
import kotlinx.android.synthetic.main.view_holder_visibility_config.view.*

class VisibilityConfigViewHolder(parent: ViewGroup) : BaseConfigViewHolder(parent, R.layout.view_holder_visibility_config) {
    companion object : HolderType {
        override fun getType(): Int = 1
    }

    init {
        itemView.apply {
            bind(dateSwitch, Type.DATE)
            bind(digitalSwitch, Type.DIGITAL)
        }
    }

    private fun bind(switch: Switch, type: Int) = switch.apply {
        isChecked = PreferenceHelper.isVisible(context, type)
        setOnCheckedChangeListener { _, checked -> sendEvent(VisibilityChangeEvent(type, checked)) }
    }
}
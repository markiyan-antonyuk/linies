package com.markantoni.linies.ui.config.holders

import android.view.ViewGroup
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.config.events.Hours24ChangeEvent
import com.markantoni.linies.ui.config.events.VisibilityChangeEvent
import com.markantoni.linies.util.sendEvent
import kotlinx.android.synthetic.main.view_holder_digital_config.view.*

class DigitalConfigViewHolder(parent: ViewGroup) : BaseConfigViewHolder(parent, R.layout.view_holder_digital_config) {
    override fun bind() {
        itemView.apply {
            visibilitySwitch.apply {
                isChecked = PreferenceHelper.isVisible(context, Type.DIGITAL)
                setOnCheckedChangeListener { _, checked -> sendEvent(VisibilityChangeEvent(Type.DIGITAL, checked)) }
            }
            formatTv.apply {
                isChecked = PreferenceHelper.is24Hours(context, Type.DIGITAL)
                setOnCheckedChangeListener { _, checked -> sendEvent(Hours24ChangeEvent(Type.DIGITAL, checked)) }
            }
        }
    }
}
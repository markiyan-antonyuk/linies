package com.markantoni.linies.ui.config.holders

import android.view.ViewGroup
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.ui.config.events.VisibilityChangeEvent
import com.markantoni.linies.util.sendEvent
import kotlinx.android.synthetic.main.view_holder_date_config.view.*

class DateConfigViewHolder(parent: ViewGroup) : BaseConfigViewHolder(parent, R.layout.view_holder_date_config) {
    override fun bind() {
        itemView.apply {
            visibilitySwitch.apply {
                isChecked = PreferenceHelper.isVisible(context, Type.DATE)
                setOnCheckedChangeListener { _, checked -> sendEvent(VisibilityChangeEvent(Type.DATE, checked)) }
            }
        }
    }
}
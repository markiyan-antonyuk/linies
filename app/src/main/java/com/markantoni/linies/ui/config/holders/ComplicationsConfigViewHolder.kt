package com.markantoni.linies.ui.config.holders

import android.view.ViewGroup
import com.markantoni.linies.Complications
import com.markantoni.linies.R
import com.markantoni.linies.ui.config.complications.ComplicationsInfoRetriever
import com.markantoni.linies.ui.config.events.OpenComplicationConfigurationEvent
import com.markantoni.linies.util.sendEvent
import kotlinx.android.synthetic.main.view_holder_complications_config.view.*

class ComplicationsConfigViewHolder(parent: ViewGroup) : BaseConfigViewHolder(parent, R.layout.view_holder_complications_config) {
    override fun bind() {
        itemView.apply {
            topIv.setImageResource(R.drawable.ic_plus)
            bottomIv.setImageResource(R.drawable.ic_plus)
            topTv.text = context.getString(R.string.config_center)
            bottomTv.text = context.getString(R.string.config_bottom)

            ComplicationsInfoRetriever.retrieveInfo(context, { id, icon, name ->
                when (id) {
                    Complications.CENTER -> {
                        topIv.setImageIcon(icon)
                        topTv.text = name
                    }
                    Complications.BOTTOM -> {
                        bottomIv.setImageIcon(icon)
                        bottomTv.text = name
                    }
                }
            })

            topLayout.setOnClickListener { sendEvent(OpenComplicationConfigurationEvent(Complications.CENTER)) }
            bottomLayout.setOnClickListener { sendEvent(OpenComplicationConfigurationEvent(Complications.BOTTOM)) }
        }
    }
}
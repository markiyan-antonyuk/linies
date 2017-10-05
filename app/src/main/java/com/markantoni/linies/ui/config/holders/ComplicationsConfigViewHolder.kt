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
            leftIv.setImageResource(R.drawable.ic_plus)
            rightIv.setImageResource(R.drawable.ic_plus)
            leftTv.text = context.getString(R.string.config_left)
            rightTv.text = context.getString(R.string.config_right)

            ComplicationsInfoRetriever.retrieveInfo(context, { id, icon, name ->
                when (id) {
                    Complications.LEFT -> {
                        leftIv.setImageIcon(icon)
                        leftTv.text = name
                    }
                    Complications.RIGHT -> {
                        rightIv.setImageIcon(icon)
                        rightTv.text = name
                    }
                }
            })

            leftLayout.setOnClickListener { sendEvent(OpenComplicationConfigurationEvent(Complications.LEFT)) }
            rightLayout.setOnClickListener { sendEvent(OpenComplicationConfigurationEvent(Complications.RIGHT)) }
        }
    }
}
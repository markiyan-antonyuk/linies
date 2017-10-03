package com.markantoni.linies.ui.config.holders

import android.support.wearable.complications.ComplicationProviderInfo
import android.support.wearable.complications.ProviderInfoRetriever
import android.view.ViewGroup
import com.markantoni.linies.Complication
import com.markantoni.linies.R
import com.markantoni.linies.util.getWatchFaceServiceComponentName
import java.util.concurrent.Executors

class ComplicationsConfigViewHolder(parent: ViewGroup) : BaseConfigViewHolder(parent, R.layout.view_holder_complications_config) {
    private val infoRetriever: ProviderInfoRetriever = ProviderInfoRetriever(itemView.context, Executors.newCachedThreadPool())

    init {
        infoRetriever.apply {
            init()
            retrieveProviderInfo(object : ProviderInfoRetriever.OnProviderInfoReceivedCallback() {
                override fun onProviderInfoReceived(id: Int, info: ComplicationProviderInfo?) {
                    //render that
                }
            }, context.getWatchFaceServiceComponentName(), *Complication.IDS)
        }
    }

    override fun release() {
        infoRetriever.release()
    }
}
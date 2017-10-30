package com.markantoni.linies.ui.config.complications

import android.content.Context
import android.graphics.drawable.Icon
import android.support.wearable.complications.ComplicationProviderInfo
import android.support.wearable.complications.ProviderInfoRetriever
import com.markantoni.linies.Complications
import com.markantoni.linies.util.getWatchFaceServiceComponentName
import java.util.concurrent.Executors

object ComplicationsInfoRetriever {
    private var infoRetriever: ProviderInfoRetriever? = null

    fun init(context: Context) {
        infoRetriever = ProviderInfoRetriever(context, Executors.newCachedThreadPool()).apply { init() }
    }

    fun retrieveInfo(context: Context, callback: (id: Int, icon: Icon, name: String) -> Unit) {
        infoRetriever?.retrieveProviderInfo(object : ProviderInfoRetriever.OnProviderInfoReceivedCallback() {
            override fun onProviderInfoReceived(id: Int, info: ComplicationProviderInfo?) {
                info?.let { callback(id, it.providerIcon, it.providerName) }
            }
        }, context.getWatchFaceServiceComponentName(), *Complications.IDS)
    }

    fun release() {
        infoRetriever?.release()
        infoRetriever = null
    }
}
package com.markantoni.linies.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.wearable.*
import com.markantoni.linies.data.DataTransfer.Companion.FALLBACK_ACTION
import com.markantoni.linies.data.DataTransfer.Companion.FALLBACK_KEY
import com.markantoni.linies.common.util.logd

interface DataTransfer {
    companion object {
        const val URI_PATH = "/com.markantoni.linies.data_transfer"
        const val KEY_DATA_MAP = "key.data.map"

        const val FALLBACK_ACTION = "fallback.intent"
        const val FALLBACK_KEY = "fallback.key"
    }

    val dataClient: DataClient
}

class DataSender(private val context: Context, private val useFallback: Boolean) : DataTransfer {
    override val dataClient: DataClient
        get() = Wearable.getDataClient(context)

    fun send(data: Bundle.() -> Unit) {
        val bundle = Bundle().apply { data() }
        logd("Sending data: $bundle")
        if (useFallback) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(FALLBACK_ACTION).apply {
                putExtra(FALLBACK_KEY, bundle)
            })
        } else {
            PutDataMapRequest.create(DataTransfer.URI_PATH).apply {
                dataMap.putDataMap(DataTransfer.KEY_DATA_MAP, DataMap.fromBundle(bundle))
                dataClient.putDataItem(asPutDataRequest().setUrgent())
            }
        }
    }
}

class DataReceiver(private val context: Context, private val useFallback: Boolean, private val onDataListener: (Bundle) -> Unit) : BroadcastReceiver(), DataTransfer, DataClient.OnDataChangedListener {
    override val dataClient: DataClient
        get() = Wearable.getDataClient(context)

    fun connect() {
        if (useFallback) {
            LocalBroadcastManager.getInstance(context).registerReceiver(this, IntentFilter(FALLBACK_ACTION))
        } else {
            dataClient.addListener(this)
        }
    }

    fun disconnect() {
        if (useFallback) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
        } else {
            dataClient.removeListener(this)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(FALLBACK_KEY)
        logd("Received data: $bundle ")
        onDataListener(bundle)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.apply {
            forEach {
                if (it.type == DataEvent.TYPE_CHANGED && it.dataItem.uri.path == DataTransfer.URI_PATH) {
                    val bundle = DataMapItem.fromDataItem(it.dataItem).dataMap.getDataMap(DataTransfer.KEY_DATA_MAP).toBundle()
                    logd("Received data: $bundle ")
                    onDataListener(bundle)
                }
            }
            release()
        }
    }
}
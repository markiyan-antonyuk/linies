package com.markantoni.linies.data.transfer

import android.content.Context
import android.os.Bundle
import com.google.android.gms.wearable.*
import com.markantoni.linies.util.logd

interface DataTransfer {
    companion object {
        const val URI_PATH = "/com.markantoni.linies.data_transfer"
        const val KEY_DATA_MAP = "key.data.map"
    }

    val dataClient: DataClient
}

class DataSender(private val context: Context) : DataTransfer {
    override val dataClient: DataClient
        get() = Wearable.getDataClient(context)

    fun send(data: Bundle.() -> Unit) {
        val bundle = Bundle().apply { data() }
        logd("Sending dataMapRequest: $bundle")
        PutDataMapRequest.create(DataTransfer.URI_PATH).apply {
            dataMap.putDataMap(DataTransfer.KEY_DATA_MAP, DataMap.fromBundle(bundle))
            dataClient.putDataItem(asPutDataRequest().setUrgent())
        }
    }
}

class DataReceiver(private val context: Context, private val onDataListener: (Bundle) -> Unit) : DataTransfer, DataClient.OnDataChangedListener {
    override val dataClient: DataClient
        get() = Wearable.getDataClient(context)

    fun connect() {
        dataClient.addListener(this)
    }

    fun disconnect() {
        dataClient.removeListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        logd("Received data changed")
        dataEvents.apply {
            forEach {
                if (it.type == DataEvent.TYPE_CHANGED && it.dataItem.uri.path == DataTransfer.URI_PATH) {
                    val bundle = DataMapItem.fromDataItem(it.dataItem).dataMap.getDataMap(DataTransfer.KEY_DATA_MAP).toBundle()
                    logd("Received dataMapRequest: $bundle ")
                    onDataListener(bundle)
                }
            }
            release()
        }
    }
}
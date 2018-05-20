package com.markantoni.linies.data.transfer

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.wearable.*
import com.markantoni.linies.util.logd

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
        dataEvents.apply {
            forEach {
                if (it.type == DataEvent.TYPE_CHANGED && it.dataItem.uri.path == DataTransfer.URI_PATH) {
                    logd("Received dataMapRequest")
                    onDataListener(DataMapItem.fromDataItem(it.dataItem).dataMap.getDataMap(DataTransfer.KEY_DATA_MAP).toBundle())
                }
            }
            release()
        }
    }
}
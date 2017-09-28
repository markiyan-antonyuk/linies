package com.markantoni.linies.data.transfer

import android.content.Context
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.*
import com.markantoni.linies.util.logd

open class DataReceiver(context: Context, private val onDataListener: (Bundle) -> Unit) : DataTransfer, DataApi.DataListener {

    private val apiClient = GoogleApiClient.Builder(context, this, this)
            .addApi(Wearable.API)
            .build()

    override fun connect() {
        apiClient.connect()
    }

    override fun disconnect() {
        apiClient.apply {
            if (isConnected) {
                Wearable.DataApi.removeListener(this, this@DataReceiver)
                disconnect()
            }
        }
    }

    override fun onConnected(bundle: Bundle?) {
        super.onConnected(bundle)
        Wearable.DataApi.addListener(apiClient, this)
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
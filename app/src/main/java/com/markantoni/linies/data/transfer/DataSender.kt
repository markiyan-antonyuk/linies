package com.markantoni.linies.data.transfer

import android.content.Context
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.util.logd

class DataSender(context: Context) : DataTransfer {
    private val apiClient = GoogleApiClient.Builder(context, this, this)
            .addApi(Wearable.API)
            .build()

    override fun connect() {
        apiClient.connect()
    }

    override fun disconnect() {
        apiClient.apply { if (isConnected) disconnect() }
    }

    fun send(bundle: Bundle) {
        logd("Sending dataMapRequest")
        PutDataMapRequest.create(DataTransfer.URI_PATH).apply {
            dataMap.putDataMap(DataTransfer.KEY_DATA_MAP, DataMap.fromBundle(bundle))
            Wearable.DataApi.putDataItem(apiClient, asPutDataRequest())
        }
    }
}
package com.markantoni.linies.data.transfer

import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
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

    fun createDataMapRequest() = PutDataMapRequest.create(DataTransfer.URI_PATH)

    fun send(dataMapRequest: PutDataMapRequest) {
        logd("Sending dataMapRequest")
        Wearable.DataApi.putDataItem(apiClient, dataMapRequest.asPutDataRequest())
    }
}
package com.markantoni.linies.data.transfer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.util.logd

class DataSender(private val context: Context) : DataTransfer {
    private val apiClient = GoogleApiClient.Builder(context, this, this)
            .addApi(Wearable.API)
            .build()

    private var useFallbackMethod = false

    override fun connect() {
        useFallbackMethod = false
        apiClient.connect()
    }

    override fun disconnect() {
        if (!useFallbackMethod) apiClient.apply { if (isConnected) disconnect() }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        logd("Using fallback data sender")
        useFallbackMethod = true
    }

    fun send(bundle: Bundle) {
        if (useFallbackMethod) {
            logd("Sending dataMapRequest as fallback")
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(DataTransfer.ACTION_FALLBACK_SEND).apply {
                putExtra(DataTransfer.KEY_FALLBACK_EXTRA, bundle)
            })
        } else {
            logd("Sending dataMapRequest")
            PutDataMapRequest.create(DataTransfer.URI_PATH).apply {
                dataMap.putDataMap(DataTransfer.KEY_DATA_MAP, DataMap.fromBundle(bundle))
                Wearable.DataApi.putDataItem(apiClient, asPutDataRequest())
            }
        }
    }
}
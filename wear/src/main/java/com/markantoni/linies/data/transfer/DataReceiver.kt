package com.markantoni.linies.data.transfer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.*
import com.markantoni.linies.util.logd

class DataReceiver(private val context: Context, private val onDataListener: (Bundle) -> Unit) : DataTransfer, DataApi.DataListener, BroadcastReceiver() {

    private val apiClient = GoogleApiClient.Builder(context, this, this)
            .addApi(Wearable.API)
            .build()

    private var useFallbackMethod = false

    override fun connect() {
        apiClient.connect()
    }

    override fun disconnect() {
        if (useFallbackMethod) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
        } else {
            apiClient.apply {
                if (isConnected) {
                    Wearable.DataApi.removeListener(this, this@DataReceiver)
                    disconnect()
                }
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        logd("Registered fallback data receiver")
        useFallbackMethod = true
        LocalBroadcastManager.getInstance(context).registerReceiver(this, IntentFilter(DataTransfer.ACTION_FALLBACK_SEND))
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

    override fun onReceive(context: Context, intent: Intent) {
        logd("Received dataMapRequest as fallback")
        onDataListener(intent.getBundleExtra(DataTransfer.KEY_FALLBACK_EXTRA))
    }
}
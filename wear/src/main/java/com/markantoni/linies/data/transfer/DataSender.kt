package com.markantoni.linies.data.transfer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.util.logd

class DataSender(private val context: Context) : DataTransfer {
    override val dataClient: DataClient
        get() = Wearable.getDataClient(context)

    fun send(data: Bundle.() -> Unit) {
        val bundle = Bundle().apply { data() }
        logd("Sending dataMapRequest")
        PutDataMapRequest.create(DataTransfer.URI_PATH).apply {
            dataMap.putDataMap(DataTransfer.KEY_DATA_MAP, DataMap.fromBundle(bundle))
            dataClient.putDataItem(asPutDataRequest())
        }
    }
}
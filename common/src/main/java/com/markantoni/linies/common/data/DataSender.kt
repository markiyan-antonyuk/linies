package com.markantoni.linies.common.data

import android.content.Context
import android.os.Bundle
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.common.util.EXECUTOR
import com.markantoni.linies.common.util.logd

class DataSender(private val context: Context) : DataTransfer {

    private val dataClient by lazy { Wearable.getDataClient(context) }
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    fun send(data: Bundle.() -> Unit) {
        val bundle = Bundle().apply { data() }
        logd("Sending data: $bundle")
        PutDataMapRequest.create(DataTransfer.URI_DATA_PATH).apply {
            dataMap.putDataMap(DataTransfer.KEY_DATA_MAP, DataMap.fromBundle(bundle))
            dataClient.putDataItem(asPutDataRequest().setUrgent())
        }
    }

    fun send(message: String) = EXECUTOR.submit {
        logd("Sending message: $message")
        val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes).map { it.id }
        nodes.forEach { messageClient.sendMessage(it, DataTransfer.URI_MESSAGE_PATH, message.toByteArray()) }
    }
}


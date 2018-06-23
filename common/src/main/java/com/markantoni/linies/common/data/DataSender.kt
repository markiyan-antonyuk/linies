package com.markantoni.linies.common.data

import android.content.Context
import android.os.Bundle
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.common.util.EXECUTOR
import com.markantoni.linies.common.util.logd

class DataSender(private val context: Context) : DataTransfer {

    private val dataClient by lazy { Wearable.getDataClient(context) }
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    fun send(protocol: DataProtocol, data: Bundle.() -> Unit) {
        val bundle = Bundle().apply { data() }
        logd("Sending data [$protocol]: $bundle")
        createPutDataMapRequest(protocol)?.let {
            it.dataMap.putDataMap(DataTransfer.KEY_DATA_MAP, DataMap.fromBundle(bundle))
            dataClient.putDataItem(it.setUrgent().asPutDataRequest())
        }
    }

    fun send(protocol: DataProtocol, message: String) = EXECUTOR.submit {
        logd("Sending message [$protocol]: $message")
        val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes).map { it.id }
        nodes.forEach { messageClient.sendMessage(it, protocol, message.toByteArray()) }
    }

    private fun createPutDataMapRequest(protocol: DataProtocol) = when (protocol) {
        DataProtocol.COMPANION -> PutDataMapRequest.create(DataTransfer.COMPANION_DATA_PATH)
        DataProtocol.WEAR -> PutDataMapRequest.create(DataTransfer.WEAR_DATA_PATH)
        DataProtocol.NONE -> null
    }

    private fun MessageClient.sendMessage(nodeId: String, protocol: DataProtocol, message: ByteArray) {
        when (protocol) {
            DataProtocol.COMPANION -> sendMessage(nodeId, DataTransfer.COMPANION_MESSAGE_PATH, message)
            DataProtocol.WEAR -> sendMessage(nodeId, DataTransfer.WEAR_MESSAGE_PATH, message)
            DataProtocol.NONE -> Unit
        }
    }
}


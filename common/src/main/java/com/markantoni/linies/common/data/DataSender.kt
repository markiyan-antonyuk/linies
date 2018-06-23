package com.markantoni.linies.common.data

import android.content.Context
import android.os.Bundle
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import com.markantoni.linies.common.util.EXECUTOR
import com.markantoni.linies.common.util.logd

class DataSender(private val context: Context) : DataTransfer {

    private val dataClient by lazy { Wearable.getDataClient(context) }
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    fun send(protocol: DataProtocol = DataProtocol.BOTH, data: Bundle.() -> Unit) {
        val bundle = Bundle().apply { data() }
        logd("Sending data [$protocol]: $bundle")
        createPutDataMapRequests(protocol).forEach {
            it.dataMap.putDataMap(DataTransfer.KEY_DATA_MAP, DataMap.fromBundle(bundle))
            dataClient.putDataItem(it.asPutDataRequest().setUrgent())
        }
    }

    fun send(message: String, protocol: DataProtocol = DataProtocol.BOTH) = EXECUTOR.submit {
        logd("Sending message [$protocol]: $message")
        val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes).map { it.id }
        nodes.forEach { messageClient.sendMessage(it, protocol, message.toByteArray()) }
    }

    private fun createPutDataMapRequests(protocol: DataProtocol) = when (protocol) {
        DataProtocol.COMPANION -> listOf(PutDataMapRequest.create(DataTransfer.COMPANION_DATA_PATH))
        DataProtocol.WEAR -> listOf(PutDataMapRequest.create(DataTransfer.WEAR_DATA_PATH))
        DataProtocol.BOTH ->
            listOf(PutDataMapRequest.create(DataTransfer.COMPANION_DATA_PATH), PutDataMapRequest.create(DataTransfer.WEAR_DATA_PATH))
    }

    private fun MessageClient.sendMessage(nodeId: String, protocol: DataProtocol, message: ByteArray) {
        when (protocol) {
            DataProtocol.COMPANION -> sendMessage(nodeId, DataTransfer.COMPANION_MESSAGE_PATH, message)
            DataProtocol.WEAR -> sendMessage(nodeId, DataTransfer.WEAR_MESSAGE_PATH, message)
            DataProtocol.BOTH -> {
                sendMessage(nodeId, DataTransfer.COMPANION_MESSAGE_PATH, message)
                sendMessage(nodeId, DataTransfer.WEAR_MESSAGE_PATH, message)
            }
        }
    }
}


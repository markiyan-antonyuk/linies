package com.markantoni.linies.common.data

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.configuration.toMap
import com.markantoni.linies.common.util.EXECUTOR
import com.markantoni.linies.common.util.logd
import com.markantoni.linies.common.util.toByteArray

class DataSender(private val context: Context) : DataTransfer {
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    fun send(protocol: DataTransfer.Protocol, configuration: Configuration) {
        val type = DataTransfer.MessageType.Config(protocol)
        if (protocol == DataTransfer.Protocol.LOCAL) {
            logd("Sending $type")
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(DataTransfer.BASE_PATH).apply {
                putExtra(DataTransfer.LOCAL_CONFIG_PATH, configuration)
            })
        } else {
            logd("Sending $type")
            messageClient.sendMessage(type, configuration.toMap().toByteArray())
        }
    }

    fun send(protocol: DataTransfer.Protocol, message: String) {
        val type = DataTransfer.MessageType.Message(protocol)
        if (protocol == DataTransfer.Protocol.LOCAL) {
            logd("Sending $type: $message")
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(DataTransfer.BASE_PATH).apply {
                putExtra(DataTransfer.LOCAL_MESSAGE_PATH, message)
            })
        } else {
            logd("Sending $type: $message")
            messageClient.sendMessage(type, message.toByteArray())
        }
    }

    private fun MessageClient.sendMessage(type: DataTransfer.MessageType, message: ByteArray) =
            EXECUTOR.submit {
                val nodes = Tasks.await(Wearable.getNodeClient(context).connectedNodes).map { it.id }
                nodes.forEach { sendMessage(it, type.uri, message) }
            }
}
package com.markantoni.linies.common.data

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.configuration.toMap
import com.markantoni.linies.common.data.DataTransfer.Companion.BASE_PATH
import com.markantoni.linies.common.data.DataTransfer.Companion.LOCAL_CONFIG_PATH
import com.markantoni.linies.common.data.DataTransfer.Companion.LOCAL_MESSAGE_PATH
import com.markantoni.linies.common.util.logd
import com.markantoni.linies.common.util.toByteArray
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class DataSender(private val context: Context, override val protocol: Protocol) : DataTransfer {
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    fun send(configuration: Configuration) {
        val type = MessageType.Config(protocol)
        if (protocol is Protocol.Remote) {
            logd("Sending $type")
            messageClient.sendMessage(type, configuration.toMap().toByteArray(), protocol.nodeId)
        } else {
            logd("Sending $type")
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(BASE_PATH).apply {
                putExtra(LOCAL_CONFIG_PATH, configuration)
            })
        }
    }

    fun send(message: String) {
        val type = MessageType.Message(protocol)
        if (protocol is Protocol.Remote) {
            logd("Sending $type: $message")
            messageClient.sendMessage(type, message.toByteArray(), protocol.nodeId)
        } else {
            logd("Sending $type: $message")
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(BASE_PATH).apply {
                putExtra(LOCAL_MESSAGE_PATH, message)
            })
        }
    }

    private fun MessageClient.sendMessage(type: MessageType, message: ByteArray, nodeId: String?) = launch(CommonPool) {
        val nodes = if (nodeId == null) Tasks.await(Wearable.getNodeClient(context).connectedNodes).map { it.id } else listOf(nodeId)
        nodes.forEach { sendMessage(it, type.uri, message) }
    }
}
package com.markantoni.linies.common.data

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.data.DataTransfer.Companion.BASE_PATH
import com.markantoni.linies.common.util.logd
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

class DataSender(private val context: Context, override val protocol: Protocol) : DataTransfer {
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    fun send(text: String? = null, configuration: Configuration? = null, filter: String = Message.FILTER_ANY, respondBack: Boolean = false) {
        val message = Message(text, configuration, filter,
                (protocol as? Protocol.Remote)?.nodeId ?: "", respondBack)
        if (protocol is Protocol.Remote) {
            messageClient.sendMessage(message)
        } else {
            logd("Sending $protocol $message")
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(BASE_PATH).apply {
                putExtra(DataTransfer.LOCAL_PATH, message.toJson())
            })
        }
    }

    private fun MessageClient.sendMessage(message: Message) = launch(CommonPool) {
        val nodes = if (message.senderId.isEmpty()) Tasks.await(Wearable.getNodeClient(context).connectedNodes).map { it.id } else listOf(message.senderId)
        nodes.forEach {
            message.senderId = it
            logd("Sending $protocol $message")
            sendMessage(it, DataTransfer.REMOTE_PATH, message.toBytes())
        }
    }
}

fun DataSender.sendConfiguration(configuration: Configuration) = send(configuration = configuration, filter = Message.FILTER_CONFIG)
fun DataSender.sendText(text: String) = send(text = text, filter = Message.FILTER_TEXT)
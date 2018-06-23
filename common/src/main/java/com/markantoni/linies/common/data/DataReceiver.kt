package com.markantoni.linies.common.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.wearable.*
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.configuration.toConfiguration
import com.markantoni.linies.common.util.logd
import com.markantoni.linies.common.util.toMap

class DataReceiver(private val context: Context) : DataTransfer, MessageClient.OnMessageReceivedListener, BroadcastReceiver() {
    private lateinit var messageClient: MessageClient
    private lateinit var type: DataTransfer.MessageType

    private var messageListener: ((String) -> Unit)? = null
    private var configListener: ((Configuration) -> Unit)? = null

    fun listen(type: DataTransfer.MessageType, message: ((String) -> Unit)? = null, config: ((Configuration) -> Unit)? = null) {
        disconnect()

        this.type = type
        if (type.protocol == DataTransfer.Protocol.LOCAL) {
            LocalBroadcastManager.getInstance(context).registerReceiver(this, IntentFilter(DataTransfer.BASE_PATH))
        } else {
            if (!::messageClient.isInitialized) {
                messageClient = Wearable.getMessageClient(context)
                messageClient.addListener(this)
            }
        }

        messageListener = message
        configListener = config
    }

    fun disconnect() {
        if (::messageClient.isInitialized) messageClient.removeListener(this)
        try {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
        } catch (e: Exception) {
            //ignore
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra(DataTransfer.LOCAL_MESSAGE_PATH)) {
            val message = intent.getStringExtra(DataTransfer.LOCAL_MESSAGE_PATH)
            logd("Local message [${type.protocol}] received: $message")
            messageListener?.invoke(message)
        }
        if (intent.hasExtra(DataTransfer.LOCAL_CONFIG_PATH)) {
            val configuration: Configuration = intent.getParcelableExtra(DataTransfer.LOCAL_CONFIG_PATH)
            logd("Local Config [${type.protocol}] received: $configuration")
            configListener?.invoke(configuration)
        }
    }

    override fun onMessageReceived(message: MessageEvent) {
        val type = when (message.path) {
            DataTransfer.REMOTE_CONFIG_PATH -> DataTransfer.MessageType.Config(DataTransfer.Protocol.REMOTE)
            DataTransfer.REMOTE_MESSAGE_PATH -> DataTransfer.MessageType.Message(DataTransfer.Protocol.REMOTE)
            else -> error("Not supported")
        }
        if (this.type != type) return

        when (type) {
            is DataTransfer.MessageType.Config -> {
                val configuration = message.data.toMap().toConfiguration()
                logd("Config [${type.protocol}] received: $configuration")
                configListener?.invoke(configuration)
            }
            is DataTransfer.MessageType.Message -> {
                val message = String(message.data)
                logd("Message [${type.protocol}] received: $message")
                messageListener?.invoke(message)
            }
        }
    }
}
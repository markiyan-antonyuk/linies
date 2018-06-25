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
import kotlin.reflect.KClass

class DataReceiver(private val context: Context, override val protocol: Protocol) : DataTransfer, MessageClient.OnMessageReceivedListener, BroadcastReceiver() {
    private lateinit var messageClient: MessageClient
    private lateinit var type: KClass<out MessageType>

    private var messageListener: ((String) -> Unit)? = null
    private var configListener: ((Configuration) -> Unit)? = null

    fun listen(type: KClass<out MessageType>, message: ((String) -> Unit)? = null, config: ((Configuration) -> Unit)? = null) {
        this.type = type
        if (protocol is Protocol.Local) {
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
            logd("Message [$protocol] received: $message")
            messageListener?.invoke(message)
        }
        if (intent.hasExtra(DataTransfer.LOCAL_CONFIG_PATH)) {
            val configuration: Configuration = intent.getParcelableExtra(DataTransfer.LOCAL_CONFIG_PATH)
            logd("Config [$protocol] received: $configuration")
            configListener?.invoke(configuration)
        }
    }

    override fun onMessageReceived(message: MessageEvent) {
        val type = when (message.path) {
            DataTransfer.REMOTE_CONFIG_PATH -> MessageType.Config(Protocol.Remote())
            DataTransfer.REMOTE_MESSAGE_PATH -> MessageType.Message(Protocol.Remote())
            else -> error("Not supported")
        }

        when (type) {
            is MessageType.Config -> {
                val configuration = message.data.toMap().toConfiguration()
                logd("Config [${type.protocol}] received: $configuration")
                configListener?.invoke(configuration)
            }
            is MessageType.Message -> {
                val received = String(message.data)
                logd("Message [${type.protocol}] received: $received")
                messageListener?.invoke(received)
            }
        }
    }
}
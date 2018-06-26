package com.markantoni.linies.common.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.common.util.logd

class DataReceiver(private val context: Context, override val protocol: Protocol) : DataTransfer, MessageClient.OnMessageReceivedListener, BroadcastReceiver() {
    private lateinit var messageClient: MessageClient

    private var listeners: MutableList<Pair<Message.Type, (Message) -> Unit>> = mutableListOf()

    fun listen(type: Message.Type, listener: ((Message) -> Unit)) {
        if (protocol is Protocol.Local) {
            LocalBroadcastManager.getInstance(context).registerReceiver(this, IntentFilter(DataTransfer.BASE_PATH))
        } else {
            if (!::messageClient.isInitialized) {
                messageClient = Wearable.getMessageClient(context)
                messageClient.addListener(this)
            }
        }

        listeners.add(type to listener)
    }

    fun disconnect() {
        listeners.clear()
        if (::messageClient.isInitialized) messageClient.removeListener(this)
        try {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
        } catch (e: Exception) {
            //ignore
        }
    }

    private fun filterAndFire(message: Message) {
        listeners
                .filter { it.first == message.type }
                .onEach { logd("Message [$protocol] received: $message") }
                .forEach { it.second.invoke(message) }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra(DataTransfer.LOCAL_PATH)) {
            val message = intent.getStringExtra(DataTransfer.LOCAL_PATH)
            filterAndFire(Message.fromJson(message))
        }
    }

    override fun onMessageReceived(event: MessageEvent) = filterAndFire(Message.fromBytes(event.data))
}
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

    private var filter = Message.FILTER_ANY
    private var listener: ((Message) -> Unit)? = null

    fun listen(filter: String = Message.FILTER_ANY, listener: ((Message) -> Unit)) {

        if (protocol is Protocol.Local) {
            LocalBroadcastManager.getInstance(context).registerReceiver(this, IntentFilter(DataTransfer.BASE_PATH))
        } else {
            if (!::messageClient.isInitialized) {
                messageClient = Wearable.getMessageClient(context)
                messageClient.addListener(this)
            }
        }

        this.filter = filter
        this.listener = listener
    }

    fun disconnect() {
        if (::messageClient.isInitialized) messageClient.removeListener(this)
        try {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
        } catch (e: Exception) {
            //ignore
        }
    }

    private fun filterAndFire(message: Message) {
        if (filter == Message.FILTER_ANY || message.filter == filter) {
            logd("Message [$protocol] received: $message")
            listener?.invoke(message)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra(DataTransfer.LOCAL_PATH)) {
            val message = intent.getStringExtra(DataTransfer.LOCAL_PATH)
            filterAndFire(Message.fromJson(message))
        }
    }

    override fun onMessageReceived(event: MessageEvent) = filterAndFire(Message.fromBytes(event.data))
}
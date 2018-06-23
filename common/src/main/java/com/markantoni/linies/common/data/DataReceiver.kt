package com.markantoni.linies.common.data

import android.content.Context
import android.os.Bundle
import com.google.android.gms.wearable.*
import com.markantoni.linies.common.util.logd

class DataReceiver(private val context: Context) : DataTransfer, DataClient.OnDataChangedListener, MessageClient.OnMessageReceivedListener {

    private var dataClient: DataClient? = null
    private var messageClient: MessageClient? = null

    private var dataListener: ((Bundle) -> Unit)? = null
    private var messageListener: ((String) -> Unit)? = null

    private var dataProtocol = DataProtocol.BOTH
    private var messageProtocol = DataProtocol.BOTH

    fun listenData(protocol: DataProtocol = DataProtocol.BOTH, listener: (Bundle) -> Unit) {
        dataClient = Wearable.getDataClient(context)
        dataListener = listener
        dataProtocol = protocol
        dataClient?.addListener(this)
    }

    fun listenMessages(protocol: DataProtocol = DataProtocol.BOTH, listener: (String) -> Unit) {
        messageClient = Wearable.getMessageClient(context)
        messageListener = listener
        messageProtocol = protocol
        messageClient?.addListener(this)
    }

    fun disconnect() {
        dataClient?.removeListener(this)
        messageClient?.removeListener(this)
    }

    override fun onMessageReceived(message: MessageEvent) {
        val shouldHandle = when (messageProtocol) {
            DataProtocol.COMPANION -> message.path == DataTransfer.COMPANION_MESSAGE_PATH
            DataProtocol.WEAR -> message.path == DataTransfer.WEAR_MESSAGE_PATH
            DataProtocol.BOTH ->
                message.path == DataTransfer.COMPANION_MESSAGE_PATH || message.path == DataTransfer.WEAR_MESSAGE_PATH
        }

        if (shouldHandle) {
            val data = String(message.data)
            logd("Received message [$messageProtocol]: $data ")
            messageListener?.invoke(data)
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.apply {
            forEach {
                val shouldHandle = when (dataProtocol) {
                    DataProtocol.COMPANION -> it.dataItem.uri.path == DataTransfer.COMPANION_DATA_PATH
                    DataProtocol.WEAR -> it.dataItem.uri.path == DataTransfer.WEAR_DATA_PATH
                    DataProtocol.BOTH ->
                        it.dataItem.uri.path == DataTransfer.COMPANION_DATA_PATH || it.dataItem.uri.path == DataTransfer.WEAR_DATA_PATH
                }
                if (it.type == DataEvent.TYPE_CHANGED && shouldHandle) {
                    val bundle = DataMapItem.fromDataItem(it.dataItem).dataMap.getDataMap(DataTransfer.KEY_DATA_MAP).toBundle()
                    logd("Received data [$dataProtocol]: $bundle ")
                    dataListener?.invoke(bundle)
                }
            }
            release()
        }
    }
}
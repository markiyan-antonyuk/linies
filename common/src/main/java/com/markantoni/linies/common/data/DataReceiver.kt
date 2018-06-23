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

    fun listenData(listener: (Bundle) -> Unit) {
        dataClient = Wearable.getDataClient(context)
        dataListener = listener
        dataClient?.addListener(this)
    }

    fun listenMessages(listener: (String) -> Unit) {
        messageClient = Wearable.getMessageClient(context)
        messageListener = listener
        messageClient?.addListener(this)
    }

    fun disconnect() {
        dataClient?.removeListener(this)
        messageClient?.removeListener(this)
    }

    override fun onMessageReceived(message: MessageEvent) {
        if (message.path == DataTransfer.URI_MESSAGE_PATH) {
            val data = String(message.data)
            logd("Received message: $data ")
            messageListener?.invoke(data)
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.apply {
            forEach {
                if (it.type == DataEvent.TYPE_CHANGED && it.dataItem.uri.path == DataTransfer.URI_DATA_PATH) {
                    val bundle = DataMapItem.fromDataItem(it.dataItem).dataMap.getDataMap(DataTransfer.KEY_DATA_MAP).toBundle()
                    logd("Received data: $bundle ")
                    dataListener?.invoke(bundle)
                }
            }
            release()
        }
    }
}
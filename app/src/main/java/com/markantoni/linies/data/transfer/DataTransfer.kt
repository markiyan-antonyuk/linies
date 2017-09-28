package com.markantoni.linies.data.transfer

import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.markantoni.linies.util.logd

interface DataTransfer : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    companion object {
        const val URI_PATH = "/com.markantoni.linies.data_transfer"
        const val KEY_DATA_MAP = "key.data.map"
    }

    fun connect()
    fun disconnect()

    override fun onConnected(bundle: Bundle?) {
        logd("${javaClass.simpleName} connected")
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        logd("${javaClass.simpleName} connection failed")
    }

    override fun onConnectionSuspended(reason: Int) {
        logd("${javaClass.simpleName} connection suspended")
    }
}
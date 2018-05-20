package com.markantoni.linies.data.transfer

import com.google.android.gms.wearable.DataClient

interface DataTransfer {
    companion object {
        const val URI_PATH = "/com.markantoni.linies.data_transfer"
        const val KEY_DATA_MAP = "key.data.map"
    }

    val dataClient: DataClient
}
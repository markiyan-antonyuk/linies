package com.markantoni.linies.data.transfer

import android.os.Bundle
import com.google.android.gms.wearable.DataClient
import com.markantoni.linies.Key
import com.markantoni.linies.Type

interface DataTransfer {
    companion object {
        const val URI_PATH = "/com.markantoni.linies.data_transfer"
        const val KEY_DATA_MAP = "key.data.map"
    }

    val dataClient: DataClient
}

var Bundle.type: Int
    set(value) = putInt(Key.TYPE, value)
    get() = getInt(Key.TYPE, Type.UNKNOWN)
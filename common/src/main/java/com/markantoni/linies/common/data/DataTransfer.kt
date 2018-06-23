package com.markantoni.linies.common.data

interface DataTransfer {
    companion object {
        const val URI_DATA_PATH = "/com.markantoni.linies.data"
        const val URI_MESSAGE_PATH = "/com.markantoni.linies.message"

        const val KEY_DATA_MAP = "key.data.map"
        const val MESSAGE_REQUEST_CONFIGURATION = "message.request.configuration"
    }
}
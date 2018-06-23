package com.markantoni.linies.common.data

interface DataTransfer {
    companion object {
        private const val BASE_PATH = "/com.markantoni.linies"
        const val WEAR_DATA_PATH = "$BASE_PATH.wear.data"
        const val COMPANION_DATA_PATH = "$BASE_PATH.companion.data"
        const val WEAR_MESSAGE_PATH = "$BASE_PATH.wear.message"
        const val COMPANION_MESSAGE_PATH = "$BASE_PATH.companion.message"

        const val KEY_DATA_MAP = "key.data.map"
        const val MESSAGE_REQUEST_CONFIGURATION = "message.request.configuration"
    }
}

enum class DataProtocol {
    WEAR, COMPANION, BOTH
}
package com.markantoni.linies.common.data

interface DataTransfer {
    companion object {
        const val BASE_PATH = "/com.markantoni.linies"

        const val LOCAL_PATH = "$BASE_PATH.local"
        const val REMOTE_PATH = "$BASE_PATH.remote"

        const val MESSAGE_REQUEST_CONFIGURATION = "text.request.configuration"
    }

    val protocol: Protocol
}

sealed class Protocol {
    companion object {
        const val VERSION = 1
    }

    class Local : Protocol() {
        override fun toString(): String = "Local"
    }

    class Remote(val nodeId: String? = null) : Protocol() {
        override fun toString(): String = "Remote"
    }
}
package com.markantoni.linies.common.data

interface DataTransfer {
    companion object {
        const val BASE_PATH = "/com.markantoni.linies"

        const val LOCAL_CONFIG_PATH = "$BASE_PATH.wear.config"
        const val REMOTE_CONFIG_PATH = "$BASE_PATH.companion.config"

        const val LOCAL_MESSAGE_PATH = "$BASE_PATH.wear.message"
        const val REMOTE_MESSAGE_PATH = "$BASE_PATH.companion.message"

        const val MESSAGE_REQUEST_CONFIGURATION = "message.request.configuration"
    }

    val protocol: Protocol
}

sealed class Protocol {

    class Local : Protocol() {
        override fun toString(): String = "Local"
    }

    class Remote(val nodeId: String? = null) : Protocol() {
        override fun toString(): String = "Remote"
    }
}

sealed class MessageType(val protocol: Protocol) {
    abstract val uri: String

    class Config(protocol: Protocol) : MessageType(protocol) {
        override val uri: String = when (protocol) {
            is Protocol.Local -> DataTransfer.LOCAL_CONFIG_PATH
            is Protocol.Remote -> DataTransfer.REMOTE_CONFIG_PATH
        }

        override fun toString(): String = "[Config $protocol]"
    }

    class Message(protocol: Protocol) : MessageType(protocol) {
        override val uri: String = when (protocol) {
            is Protocol.Local -> DataTransfer.LOCAL_MESSAGE_PATH
            is Protocol.Remote -> DataTransfer.REMOTE_MESSAGE_PATH
        }

        override fun toString(): String = "[Message $protocol]"
    }
}
package com.markantoni.linies.common.data

import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.util.GSON

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

class Message(
        val text: String?,
        val configuration: Configuration?,
        val filter: String = FILTER_ANY,
        var senderId: String,
        val respondBack: Boolean = false,
        val protocolVersion: Int = Protocol.VERSION
) {
    companion object {
        fun fromJson(json: String) = GSON.fromJson(json, Message::class.java)

        fun fromBytes(bytes: ByteArray) = fromJson(String(bytes))

        const val FILTER_ANY = "any"
        const val FILTER_CONFIG = "config"
        const val FILTER_TEXT = "text"
    }

    fun toJson() = GSON.toJson(this)

    fun toBytes() = toJson().toByteArray()

    override fun toString(): String {
        return "\n" +
                "Text: $text\n" +
                "Configuration: $configuration\n" +
                "Filter: $filter\n" +
                "SenderId: $senderId\n" +
                "RespondBack: $respondBack\n" +
                "ProtocolVersion: $protocolVersion"
    }
}
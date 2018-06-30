package com.markantoni.linies.common.data

import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.data.serialization.GSON

abstract class Message(
        private val respondBack: Boolean,
        val type: Type,
        private val protocolVersion: Int = Protocol.VERSION) {

    enum class Type {
        TEXT, CONFIG
    }

    companion object {
        inline fun <reified T : Message> fromJson(json: String): T = GSON.fromJson(json, T::class.java)

        inline fun <reified T : Message> fromBytes(bytes: ByteArray): T = fromJson(String(bytes))
    }

    fun toJson() = GSON.toJson(this)

    fun toBytes() = toJson().toByteArray()

    override fun toString(): String {
        return "\n" +
                "Type: $type\n" +
                "RespondBack: $respondBack\n" +
                "ProtocolVersion: $protocolVersion"
    }
}

class TextMessage(val text: String, respondBack: Boolean = false) :
        Message(respondBack, Type.TEXT) {

    override fun toString(): String = "${super.toString()}\n" +
            "Text: $text"
}

class ConfigurationMessage(val configuration: Configuration, respondBack: Boolean = false) :
        Message(respondBack, Type.CONFIG) {

    override fun toString(): String = "${super.toString()}\n" +
            "Configuration: $configuration"
}
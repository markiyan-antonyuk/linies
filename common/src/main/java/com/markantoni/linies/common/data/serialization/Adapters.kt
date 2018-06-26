package com.markantoni.linies.common.data.serialization

import com.google.gson.*
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.data.ConfigurationMessage
import com.markantoni.linies.common.data.Message
import com.markantoni.linies.common.data.TextMessage
import java.lang.reflect.Type

class ConfigurationSerializer : JsonSerializer<Configuration> {
    override fun serialize(src: Configuration, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val result = JsonObject()
        src.write(
                { k, i -> result.addProperty(k, i) },
                { k, b -> result.addProperty(k, b) },
                { k, s -> result.addProperty(k, s) })
        return result
    }
}

class ConfigurationDeserializer : JsonDeserializer<Configuration> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Configuration {
        val src = json.asJsonObject
        return Configuration.read(
                { src.get(it).asInt },
                { src.get(it).asBoolean },
                { src.get(it).asString })
    }
}

class MessageDeserializer : JsonDeserializer<Message> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Message {
        val obj = json.asJsonObject
        val type = Message.Type.valueOf(obj["type"].asString)
        return when (type) {
            Message.Type.TEXT -> context.deserialize(json, TextMessage::class.java)
            Message.Type.CONFIG -> context.deserialize(json, ConfigurationMessage::class.java)
        }
    }
}
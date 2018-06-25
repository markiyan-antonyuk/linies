package com.markantoni.linies.common.configuration

import com.google.gson.*
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
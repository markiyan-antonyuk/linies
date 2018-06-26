package com.markantoni.linies.common.data.serialization

import com.google.gson.GsonBuilder
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.data.Message

val GSON by lazy {
    GsonBuilder()
            .registerTypeAdapter(Configuration::class.java, ConfigurationSerializer())
            .registerTypeAdapter(Configuration::class.java, ConfigurationDeserializer())
            .registerTypeAdapter(Message::class.java, MessageDeserializer())
            .create()
}
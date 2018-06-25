package com.markantoni.linies.common.util

import com.google.gson.GsonBuilder
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.configuration.ConfigurationDeserializer
import com.markantoni.linies.common.configuration.ConfigurationSerializer

val GSON by lazy {
    GsonBuilder()
            .registerTypeAdapter(Configuration::class.java, ConfigurationSerializer())
            .registerTypeAdapter(Configuration::class.java, ConfigurationDeserializer())
            .create()
}
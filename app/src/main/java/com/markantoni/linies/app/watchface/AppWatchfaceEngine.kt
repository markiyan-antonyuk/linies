package com.markantoni.linies.app.watchface

import android.graphics.Color
import com.markantoni.linies.common.configuration.*
import com.markantoni.linies.common.engine.CommonWatchfaceEngine

class AppWatchfaceEngine(private val view: AppWatchfaceView) : CommonWatchfaceEngine() {
    override val isVisible: Boolean get() = true

    override var isInAmbientMode: Boolean = false
    override var isCentralComplicationVisible: Boolean = false

    override var configuration: Configuration = Configuration(
            Second(Color.WHITE),
            Minute(Color.WHITE),
            Hour(Color.WHITE),
            Digital(Color.WHITE, true, true),
            Date(Color.WHITE, true, "dd:MM"),
            Complication(Color.WHITE),
            Animation(true))

    override fun invalidate() = view.invalidate()

    override fun saveConfiguration(configuration: Configuration) {
        this.configuration = configuration
    }
}
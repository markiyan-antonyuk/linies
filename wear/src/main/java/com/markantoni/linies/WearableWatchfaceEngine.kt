package com.markantoni.linies

import android.support.wearable.complications.ComplicationData
import android.support.wearable.watchface.CanvasWatchFaceService
import android.support.wearable.watchface.WatchFaceService
import android.support.wearable.watchface.WatchFaceStyle
import android.view.SurfaceHolder
import com.markantoni.linies.common.configuration.Complication.Companion.CENTER
import com.markantoni.linies.common.configuration.Configuration
import com.markantoni.linies.common.data.*
import com.markantoni.linies.common.engine.CommonWatchfaceEngine
import com.markantoni.linies.complications.Complication
import com.markantoni.linies.complications.ComplicationsDrawer
import com.markantoni.linies.preferences.Preferences
import com.markantoni.linies.util.TimeZoneReceiver
import java.util.*

class WearableWatchfaceEngine(private val service: LiniesWatchFaceService, private val engine: CanvasWatchFaceService.Engine) : CommonWatchfaceEngine() {
    override val isVisible: Boolean get() = engine.isVisible
    override val isInAmbientMode: Boolean get() = engine.isInAmbientMode
    override val configuration: Configuration get() = preferences.configuration
    override val isCentralComplicationVisible: Boolean get() = complicationsDrawer.isComplicationVisible(CENTER)

    private val preferences by lazy { Preferences(service) }
    private val timeZoneReceiver = TimeZoneReceiver { updateTimeZone(true) }
    private lateinit var complicationsDrawer: ComplicationsDrawer

    private val localReceiver by lazy { DataReceiver(service, Protocol.Local()) }
    private val removeReceiver by lazy { DataReceiver(service, Protocol.Remote()) }
    private val remoteSender by lazy { DataSender(service, Protocol.Remote()) }

    override fun onCreate(holder: SurfaceHolder) {
        super.onCreate(holder)
        engine.apply {
            setWatchFaceStyle(WatchFaceStyle.Builder(service)
                    .setCardProgressMode(WatchFaceStyle.PROGRESS_MODE_DISPLAY)
                    .setShowUnreadCountIndicator(true)
                    .setAcceptsTapEvents(true)
                    .build())

            setActiveComplications(*Complication.IDS)
        }

        complicationsDrawer = ComplicationsDrawer(service, configuration.complication.color)
        drawers.add(complicationsDrawer)

        localReceiver.listen(Message.FILTER_CONFIG) {
            it.configuration?.let {
                updateConfiguration(it)
                remoteSender.sendConfiguration(it)
            }
        }
        removeReceiver.listen(Message.FILTER_TEXT) {
            if (it.text == DataTransfer.MESSAGE_REQUEST_CONFIGURATION) {
                remoteSender.sendConfiguration(Preferences.configuration(service))
            }
        }
    }

    override fun onDestroy() {
        localReceiver.disconnect()
        removeReceiver.disconnect()
        super.onDestroy()
    }

    override fun invalidate() = engine.invalidate()

    override fun onVisibilityChanged(visible: Boolean) {
        updateTimeZone()
        timeZoneReceiver.apply { if (visible) register(service) else unregister(service) }
        super.onVisibilityChanged(visible)
    }

    fun updateComplicationData(id: Int, data: ComplicationData) {
        complicationsDrawer.update(id, data)
        invalidate()
    }

    fun handleTapCommand(tapType: Int, x: Int, y: Int, eventTime: Long) {
        if (tapType == WatchFaceService.TAP_TYPE_TAP) complicationsDrawer.handleTap(x, y)
    }

    override fun saveConfiguration(configuration: Configuration) {
        preferences.configuration = configuration
    }

    private fun updateTimeZone(invalidate: Boolean = false) {
        calendar.timeZone = TimeZone.getDefault()
        if (invalidate) invalidate()
    }
}
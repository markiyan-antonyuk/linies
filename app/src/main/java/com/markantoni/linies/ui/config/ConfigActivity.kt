package com.markantoni.linies.ui.config

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.markantoni.linies.Key
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.data.events.VisiblityChangeEvent
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.util.registerEventBus
import com.markantoni.linies.util.unregisterEventBus
import kotlinx.android.synthetic.main.activity_config.*
import org.greenrobot.eventbus.Subscribe


class ConfigActivity : Activity() {
    private val CONFIG_ITEMS by lazy {
        listOf(
                ConfigItem(getString(R.string.config_hour), Type.HOUR, false),
                ConfigItem(getString(R.string.config_minute), Type.MINUTE, false),
                ConfigItem(getString(R.string.config_second), Type.SECOND, false),
                ConfigItem(getString(R.string.config_digital), Type.DIGITAL, true),
                ConfigItem(getString(R.string.config_date), Type.DATE, true)
        )
    }

    private lateinit var dataSender: DataSender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        dataSender = DataSender(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConfigActivity)
            PagerSnapHelper().attachToRecyclerView(this)
            adapter = ConfigAdapter(CONFIG_ITEMS)
        }
    }

    @Subscribe
    fun onVisibilityChangeEvent(event: VisiblityChangeEvent) {
        dataSender.createDataMapRequest().apply {
            dataMap.apply {
                putInt(Key.TYPE, event.type)
                putBoolean(Key.VISIBLE, event.visible)
            }
            dataSender.send(this)
        }
    }

    override fun onStart() {
        super.onStart()
        dataSender.connect()
        registerEventBus()
    }

    override fun onPause() {
        dataSender.disconnect()
        unregisterEventBus()
        super.onPause()
    }
}
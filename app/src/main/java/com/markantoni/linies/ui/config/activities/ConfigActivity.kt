package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.markantoni.linies.Key
import com.markantoni.linies.R
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.ui.config.ConfigAdapter
import com.markantoni.linies.ui.config.events.OpenColorPickerEvent
import com.markantoni.linies.ui.config.events.VisibilityChangeEvent
import com.markantoni.linies.util.registerEventBus
import com.markantoni.linies.util.startActivityWithRevealAnimation
import com.markantoni.linies.util.unregisterEventBus
import kotlinx.android.synthetic.main.activity_config.*
import org.greenrobot.eventbus.Subscribe


class ConfigActivity : Activity() {
    private lateinit var dataSender: DataSender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        dataSender = DataSender(this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConfigActivity)
            PagerSnapHelper().attachToRecyclerView(this)
        }
    }

    @Subscribe
    fun onOpenColorPickerEvent(event: OpenColorPickerEvent) = startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this, event.type))

    @Subscribe
    fun onVisibilityChangeEvent(event: VisibilityChangeEvent) = dataSender.send(Bundle().apply {
        putInt(Key.TYPE, event.type)
        putBoolean(Key.VISIBLE, event.visible)
    })

    override fun onResume() {
        super.onResume()
        recyclerView.adapter = ConfigAdapter() //reload adapter
        dataSender.connect()
        registerEventBus()
    }

    override fun onPause() {
        dataSender.disconnect()
        unregisterEventBus()
        super.onPause()
    }
}
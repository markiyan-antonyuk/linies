package com.markantoni.linies.ui.config.root

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.ui.config.activities.ColorPickerActivity
import com.markantoni.linies.ui.config.activities.DigitalConfigActivity
import com.markantoni.linies.ui.config.activities.SecondsConfigActivity
import kotlinx.android.synthetic.main.activity_root_config.*

class RootConfigActivity : Activity() {
    private val items by lazy {
        listOf(
                RootConfigItem(R.string.config_seconds, createSimpleIntent(SecondsConfigActivity::class.java)),
                RootConfigItem(R.string.config_minutes, ColorPickerActivity.newIntent(this, Type.MINUTE)),
                RootConfigItem(R.string.config_hours, ColorPickerActivity.newIntent(this, Type.HOUR)),
                RootConfigItem(R.string.config_digital, createSimpleIntent(DigitalConfigActivity::class.java)),
                //fixme HERE finish
                RootConfigItem(R.string.config_date, createSimpleIntent(RootConfigActivity::class.java)),
                RootConfigItem(R.string.config_complications, createSimpleIntent(RootConfigActivity::class.java))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root_config)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RootConfigActivity)
            adapter = RootConfigAdapter(items)
        }
    }

    private fun createSimpleIntent(activityClass: Class<out Activity>) = Intent(this, activityClass)
}


package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import com.markantoni.linies.R
import kotlinx.android.synthetic.main.activity_root_config.*

class RootConfigActivity : Activity() {
    private val items = listOf(
            RootConfigItem(R.string.config_seconds, RootConfigActivity::class.java),
            RootConfigItem(R.string.config_minutes, RootConfigActivity::class.java),
            RootConfigItem(R.string.config_hours, RootConfigActivity::class.java),
            RootConfigItem(R.string.config_digital, RootConfigActivity::class.java),
            RootConfigItem(R.string.config_date, RootConfigActivity::class.java),
            RootConfigItem(R.string.config_complications, RootConfigActivity::class.java)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root_config)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RootConfigActivity)
            adapter = RootConfigAdapter(items)
        }
    }

    data class RootConfigItem(@StringRes val title: Int, val activityClass: Class<out Activity>)

}


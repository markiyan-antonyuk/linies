package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import android.widget.CheckBox
import com.markantoni.linies.*
import com.markantoni.linies.configuration.Preferences
import com.markantoni.linies.configuration.VisibleHand
import com.markantoni.linies.configuration.findHand
import com.markantoni.linies.configuration.withConfiguration
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.ui.watch.drawers.DrawerType
import kotlinx.android.synthetic.main.activity_visibility_config.*

class VisibilityConfigActivity : Activity() {
    private val dataSender by lazy { DataSender(this, true) }
    private val configuration by lazy { Preferences.configuration(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visibility_config)

        setup(configDigital, DrawerType.DIGITAL)
        setup(configDate, DrawerType.DATE)
    }

    private fun setup(item: CheckBox, type: DrawerType) = item.apply {
        val hand = configuration.findHand(type) as VisibleHand
        isChecked = hand.visible
        setOnCheckedChangeListener { _, checked ->
            dataSender.send {
                withConfiguration(this@VisibilityConfigActivity) {
                    hand.visible = checked
                }
            }
        }
    }
}
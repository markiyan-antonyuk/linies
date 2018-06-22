package com.markantoni.linies.ui

import android.app.Activity
import android.os.Bundle
import android.widget.CheckBox
import com.markantoni.linies.*
import com.markantoni.linies.preferences.Preferences
import com.markantoni.linies.common.configuration.VisibleHand
import com.markantoni.linies.common.configuration.findHand
import com.markantoni.linies.common.configuration.putConfiguration
import com.markantoni.linies.common.data.DataSender
import com.markantoni.linies.common.drawers.DrawerType
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
                hand.visible = checked
                putConfiguration(configuration)
            }
        }
    }
}
package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.markantoni.linies.configuration.Preferences
import com.markantoni.linies.R
import com.markantoni.linies.configuration.findHand
import com.markantoni.linies.ui.watch.drawers.DrawerType
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_colors_config.*

class ColorsConfigActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colors_config)
        setupViews()
    }

    private fun setupViews() {
        setupWithColor(configSeconds, DrawerType.SECOND)
        setupWithColor(configMinutes, DrawerType.MINUTE)
        setupWithColor(configHours, DrawerType.HOUR)
        setupWithColor(configDigital, DrawerType.DIGITAL)
        setupWithColor(configDate, DrawerType.DATE)
        setupWithColor(configComplications, DrawerType.COMPLICATION)
    }

    private fun setupWithColor(item: TextView, type: DrawerType) = item.apply {
        setTextColor(Preferences.configuration(this@ColorsConfigActivity).findHand(type).color)
        setOnClickListener { startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this@ColorsConfigActivity, type)) }
    }

    override fun onResume() {
        super.onResume()
        setupViews()
    }
}
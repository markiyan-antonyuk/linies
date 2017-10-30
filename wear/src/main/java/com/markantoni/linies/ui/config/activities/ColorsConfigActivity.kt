package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.preference.WatchFacePreferences
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_colors_config.*

class ColorsConfigActivity : Activity() {
    private val preferences by lazy { WatchFacePreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colors_config)
        setupViews()
    }

    private fun setupViews() {
        setupWithColor(configSeconds, Type.SECOND)
        setupWithColor(configMinutes, Type.MINUTE)
        setupWithColor(configHours, Type.HOUR)
        setupWithColor(configDigital, Type.DIGITAL)
        setupWithColor(configDate, Type.DATE)
        setupWithColor(configComplications, Type.COMPLICATIONS)
    }

    private fun setupWithColor(item: TextView, type: Int) = item.apply {
        setTextColor(preferences.getColor(type))
        setOnClickListener { startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this@ColorsConfigActivity, type)) }
    }

    override fun onResume() {
        super.onResume()
        setupViews()
    }
}
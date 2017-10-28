package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.util.showToast
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_seconds_config.*

class SecondsConfigActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seconds_config)

        colorConfig.setOnClickListener { startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this, Type.SECOND)) }
        animateConfig.setOnCheckedChangeListener { _, checked -> showToast("TODO") }
    }
}
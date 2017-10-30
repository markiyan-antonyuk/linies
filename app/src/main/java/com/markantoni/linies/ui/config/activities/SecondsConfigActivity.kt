package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import com.markantoni.linies.Key
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.data.setType
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.preference.WatchFacePreferences
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_seconds_config.*

class SecondsConfigActivity : Activity() {
    private val dataSender by lazy { DataSender(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seconds_config)

        colorConfig.setOnClickListener { startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this, Type.SECOND)) }
        animateConfig.apply {
            isChecked = WatchFacePreferences(this@SecondsConfigActivity).isAnimating()
            setOnCheckedChangeListener { _, checked -> sendUpdateAnimating(checked) }
        }
    }

    override fun onResume() {
        super.onResume()
        dataSender.connect()
    }

    override fun onPause() {
        dataSender.disconnect()
        super.onPause()
    }

    private fun sendUpdateAnimating(animating: Boolean) {
        arrayOf(Type.SECOND, Type.MINUTE, Type.HOUR).forEach { type ->
            dataSender.send {
                setType(type)
                putBoolean(Key.ANIMATING, animating)
            }
        }
    }
}
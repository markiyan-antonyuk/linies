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
import kotlinx.android.synthetic.main.activity_digital_config.*

class DigitalConfigActivity : Activity() {
    private val dataSender: DataSender by lazy { DataSender(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_config)

        colorConfig.setOnClickListener { startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this, Type.DIGITAL)) }
        visibleConfig.apply {
            isChecked = WatchFacePreferences(this@DigitalConfigActivity).isVisible(Type.DIGITAL)
            setOnCheckedChangeListener { _, checked ->
                dataSender.send {
                    setType(Type.DIGITAL)
                    putBoolean(Key.VISIBLE, checked)
                }
            }
        }
        hour24Config.apply {
            isChecked = WatchFacePreferences(this@DigitalConfigActivity).is24Hours()
            setOnCheckedChangeListener { _, checked ->
                dataSender.send {
                    setType(Type.DIGITAL)
                    putBoolean(Key.HOURS24, checked)
                }
            }
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
}
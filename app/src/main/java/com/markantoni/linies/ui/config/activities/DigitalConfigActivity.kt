package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import com.markantoni.linies.Key
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.preference.PreferenceHelper
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_digital_config.*

class DigitalConfigActivity : Activity() {
    private val dataSender: DataSender by lazy { DataSender(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_config)

        colorConfig.setOnClickListener { startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this, Type.DIGITAL)) }
        visibleConfig.apply {
            isChecked = PreferenceHelper.isVisible(this@DigitalConfigActivity, Type.DIGITAL)
            setOnCheckedChangeListener { _, checked ->
                dataSender.send {
                    putInt(Key.TYPE, Type.DIGITAL)
                    putBoolean(Key.VISIBLE, checked)
                }
            }
        }
        hour24Config.apply {
            isChecked = PreferenceHelper.is24Hours(this@DigitalConfigActivity, Type.DIGITAL)
            setOnCheckedChangeListener { _, checked ->
                dataSender.send {
                    putInt(Key.TYPE, Type.DIGITAL)
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
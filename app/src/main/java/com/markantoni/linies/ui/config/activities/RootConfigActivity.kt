package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.markantoni.linies.Key
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.data.setType
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.preference.WatchFacePreferences
import com.markantoni.linies.util.showToast
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_root_config.*
import java.text.SimpleDateFormat
import java.util.*

class RootConfigActivity : Activity() {
    private val dataSender by lazy { DataSender(this) }
    private val preferences by lazy { WatchFacePreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root_config)

        configColors.setOnClickListener { showToast("TODO") }
        configComplications.setOnClickListener { showToast("TODO") }
        configVisibility.setOnClickListener { showToast("TODO") }
        configAnimate.apply {
            isChecked = preferences.isAnimating()
            setOnCheckedChangeListener { _, checked ->
                dataSender.send {
                    setType(Type.SECOND)
                    putBoolean(Key.ANIMATING, checked)
                }
            }
        }
        config24Hours.apply {
            isChecked = preferences.is24Hours()
            setOnCheckedChangeListener { _, checked ->
                dataSender.send {
                    setType(Type.DIGITAL)
                    putBoolean(Key.HOURS24, checked)
                }
            }
        }
        configDateFormat.apply {
            setOnClickListener { startActivityWithRevealAnimation(Intent(this@RootConfigActivity, DateFormatPickerActivity::class.java)) }
            updateDateFormat()
        }
    }

    private fun updateDateFormat() {
        val format = preferences.getDateFormat()
        dateFormatHint.text = SimpleDateFormat(format, Locale.getDefault()).format(Date())
    }

    override fun onResume() {
        super.onResume()
        updateDateFormat()
        dataSender.connect()
    }

    override fun onPause() {
        dataSender.disconnect()
        super.onPause()
    }
}


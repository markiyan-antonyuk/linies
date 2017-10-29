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
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_date_config.*
import java.text.SimpleDateFormat
import java.util.*

class DateConfigActivity : Activity() {
    private val dataSender by lazy { DataSender(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_config)

        colorConfig.setOnClickListener { startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this, Type.DATE)) }
        formatConfig.setOnClickListener { startActivityWithRevealAnimation(Intent(this, DateFormatPickerActivity::class.java)) }
        visibleConfig.apply {
            isChecked = WatchFacePreferences(this@DateConfigActivity).isVisible(Type.DATE)
            setOnCheckedChangeListener { _, checked ->
                dataSender.send {
                    setType(Type.DATE)
                    putBoolean(Key.VISIBLE, checked)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataSender.connect()
        updateFormat()
    }

    override fun onPause() {
        dataSender.disconnect()
        super.onPause()
    }

    private fun updateFormat() {
        val format = WatchFacePreferences(this).getDateFormat()
        formatConfig.text = getString(R.string.config_date_format, SimpleDateFormat(format, Locale.getDefault()).format(Date()))
    }
}
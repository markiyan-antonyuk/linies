package com.markantoni.linies.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.markantoni.linies.R
import com.markantoni.linies.common.data.DataSender
import com.markantoni.linies.common.data.DataTransfer
import com.markantoni.linies.common.util.startActivityWithRevealAnimation
import com.markantoni.linies.preferences.Preferences
import com.markantoni.linies.util.configurationFrom
import kotlinx.android.synthetic.main.activity_root_config.*
import java.text.SimpleDateFormat
import java.util.*

class RootConfigActivity : Activity() {
    private val dataSender by lazy { DataSender(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root_config)
        val configuration = Preferences.configuration(this)

        configColors.setOnClickListener { openActivity(ColorsConfigActivity::class.java) }
        configComplications.setOnClickListener { openActivity(ComplicationsConfigActivity::class.java) }
        configVisibility.setOnClickListener { openActivity(VisibilityConfigActivity::class.java) }
        configAnimate.apply {
            isChecked = configuration.animation.enabled
            setOnCheckedChangeListener { _, checked ->
                dataSender.send(DataTransfer.Protocol.LOCAL,
                        configurationFrom(this@RootConfigActivity) { animation.enabled = checked })
            }
        }

        config24Hours.apply {
            isChecked = configuration.digital.is24
            setOnCheckedChangeListener { _, checked ->
                dataSender.send(DataTransfer.Protocol.LOCAL,
                        configurationFrom(this@RootConfigActivity) { digital.is24 = checked })
            }
        }
        configDateFormat.apply {
            setOnClickListener { openActivity(DateFormatPickerActivity::class.java) }
            updateDateFormat()
        }
    }

    private fun openActivity(activityClass: Class<out Activity>) = startActivityWithRevealAnimation(Intent(this, activityClass))

    private fun updateDateFormat() {
        val format = Preferences.configuration(this).date.format
        dateFormatHint.text = SimpleDateFormat(format, Locale.getDefault()).format(Date())
    }

    override fun onResume() {
        super.onResume()
        updateDateFormat()
    }
}


package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.markantoni.linies.*
import com.markantoni.linies.configuration.Preferences
import com.markantoni.linies.configuration.withConfiguration
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_root_config.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class RootConfigActivity : Activity() {
    private val dataSender by lazy { DataSender(this, true) }

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
                dataSender.send {
                    withConfiguration(this@RootConfigActivity) {
                        animation.enabled = checked
                    }
                }
            }
        }
        config24Hours.apply {
            isChecked = configuration.digital.is24
            setOnCheckedChangeListener { _, checked ->
                dataSender.send {
                    withConfiguration(this@RootConfigActivity) {
                        digital.is24 = checked
                    }
                }
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

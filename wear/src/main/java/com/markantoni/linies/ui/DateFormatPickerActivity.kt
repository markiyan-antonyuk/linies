package com.markantoni.linies.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import com.markantoni.linies.*
import com.markantoni.linies.preferences.Preferences
import com.markantoni.linies.common.data.DataSender
import com.markantoni.linies.common.util.moveElementToStart
import com.markantoni.linies.util.withConfiguration
import kotlinx.android.synthetic.main.activity_radio_group.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

class DateFormatPickerActivity : Activity() {
    private val dataSender by lazy { DataSender(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio_group)
        initDateFormats()
    }

    private fun initDateFormats() {
        val date = Date()
        val formats = resources.getStringArray(R.array.date_formats).toMutableList()
        val currentFormat = Preferences.configuration(this).date.format
        formats.moveElementToStart(currentFormat)
        formats.forEach { format ->
            (LayoutInflater.from(this).inflate(R.layout.view_date_format_item, radioGroup, false) as RadioButton).apply {
                id = View.generateViewId()
                isChecked = format == currentFormat
                text = SimpleDateFormat(format, Locale.getDefault()).format(date)
                setOnCheckedChangeListener { _, checked -> if (checked) sendUpdateDateFormat(format) }
                radioGroup.addView(this)
            }
        }
    }

    private fun sendUpdateDateFormat(format: String) = dataSender.send {
        withConfiguration(this@DateFormatPickerActivity) {
            date.format = format
        }
    }
}
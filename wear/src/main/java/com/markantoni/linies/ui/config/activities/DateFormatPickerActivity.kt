package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import com.markantoni.linies.Key
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.data.transfer.type
import com.markantoni.linies.preference.WatchFacePreferences
import com.markantoni.linies.util.logd
import com.markantoni.linies.util.moveElementToStart
import kotlinx.android.synthetic.main.activity_radio_group.*
import java.text.SimpleDateFormat
import java.util.*

class DateFormatPickerActivity : Activity() {
    private lateinit var dataSender: DataSender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio_group)
        dataSender = DataSender(this)
        initDateFormats()
    }

    private fun initDateFormats() {
        val date = Date()
        val formats = resources.getStringArray(R.array.date_formats).toMutableList()
        val currentFormat = WatchFacePreferences(this).getDateFormat()
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
        logd("Send new date format $format")
        type = Type.DATE
        putString(Key.DATE_FORMAT, format)
    }
}
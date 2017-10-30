package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import android.widget.CheckBox
import com.markantoni.linies.Key
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.data.setType
import com.markantoni.linies.data.transfer.DataSender
import com.markantoni.linies.preference.WatchFacePreferences
import kotlinx.android.synthetic.main.activity_visibility_config.*

class VisibilityConfigActivity : Activity() {
    private val dataSender by lazy { DataSender(this) }
    private val preferences by lazy { WatchFacePreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visibility_config)

        setup(configDigital, Type.DIGITAL)
        setup(configDate, Type.DATE)
    }

    private fun setup(item: CheckBox, type: Int) = item.apply {
        isChecked = preferences.isVisible(type)
        setOnCheckedChangeListener { _, checked ->
            dataSender.send {
                setType(type)
                putBoolean(Key.VISIBLE, checked)
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
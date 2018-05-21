package com.markantoni.linies.ui

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.wearable.complications.ComplicationHelperActivity
import android.widget.TextView
import com.markantoni.linies.complications.Complication
import com.markantoni.linies.R
import com.markantoni.linies.complications.ComplicationsInfoRetriever
import com.markantoni.linies.common.util.startActivityWithRevealAnimation
import com.markantoni.linies.util.getWatchFaceServiceComponentName
import kotlinx.android.synthetic.main.activity_complications_config.*

class ComplicationsConfigActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complications_config)
        ComplicationsInfoRetriever.init(this)

        configCenter.setOnClickListener { openComplicationConfig(Complication.CENTER) }
        configBottom.setOnClickListener { openComplicationConfig(Complication.BOTTOM) }

    }

    private fun updateComplicationsInfo() {
        configCenter.setupWithComplication(null, null, getString(R.string.config_center))
        configBottom.setupWithComplication(null, null, getString(R.string.config_bottom))
        ComplicationsInfoRetriever.retrieveInfo(this, { id, icon, name ->
            val drawable = icon.loadDrawable(this)
            when (id) {
                Complication.CENTER -> configCenter.setupWithComplication(drawable, name)
                Complication.BOTTOM -> configBottom.setupWithComplication(drawable, name)
            }
        })
    }

    private fun TextView.setupWithComplication(drawable: Drawable?, name: String?, fallbackName: String? = null) {
        text = name?.let { it } ?: fallbackName
        val compoundDrawable = drawable?.let { it } ?: getDrawable(R.drawable.ic_plus)
        setCompoundDrawablesWithIntrinsicBounds(compoundDrawable, null, null, null)
    }

    private fun openComplicationConfig(id: Int) {
        Complication.SUPPORTED_TYPES[id]?.let {
            startActivityWithRevealAnimation(ComplicationHelperActivity.createProviderChooserHelperIntent(this, getWatchFaceServiceComponentName(), id, *it))
        }
    }

    override fun onResume() {
        super.onResume()
        updateComplicationsInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        ComplicationsInfoRetriever.release()
    }
}
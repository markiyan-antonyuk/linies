package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.wearable.complications.ComplicationHelperActivity
import android.widget.TextView
import com.markantoni.linies.Complications
import com.markantoni.linies.R
import com.markantoni.linies.ui.config.complications.ComplicationsInfoRetriever
import com.markantoni.linies.util.getWatchFaceServiceComponentName
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_complications_config.*

class ComplicationsConfigActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complications_config)
        ComplicationsInfoRetriever.init(this)

        configCenter.setOnClickListener { openComplicationConfig(Complications.CENTER) }
        configBottom.setOnClickListener { openComplicationConfig(Complications.BOTTOM) }

    }

    private fun updateComplicationsInfo() {
        configCenter.setupWithComplication(null, null, getString(R.string.config_center))
        configBottom.setupWithComplication(null, null, getString(R.string.config_bottom))
        ComplicationsInfoRetriever.retrieveInfo(this, { id, icon, name ->
            val drawable = icon.loadDrawable(this)
            when (id) {
                Complications.CENTER -> configCenter.setupWithComplication(drawable, name)
                Complications.BOTTOM -> configBottom.setupWithComplication(drawable, name)
            }
        })
    }

    private fun TextView.setupWithComplication(drawable: Drawable?, name: String?, fallbackName: String? = null) {
        text = name?.let { it } ?: fallbackName
        val compoundDrawable = drawable?.let { it } ?: getDrawable(R.drawable.ic_plus)
        setCompoundDrawablesWithIntrinsicBounds(compoundDrawable, null, null, null)
    }

    private fun openComplicationConfig(id: Int) {
        Complications.SUPPORTED_TYPES[id]?.let {
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
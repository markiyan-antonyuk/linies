package com.markantoni.linies.ui.config.activities

import android.app.Activity
import android.os.Bundle
import android.support.wearable.complications.ComplicationHelperActivity
import com.markantoni.linies.Complications
import com.markantoni.linies.R
import com.markantoni.linies.Type
import com.markantoni.linies.ui.config.complications.ComplicationsInfoRetriever
import com.markantoni.linies.util.getWatchFaceServiceComponentName
import com.markantoni.linies.util.startActivityWithRevealAnimation
import kotlinx.android.synthetic.main.activity_complications_config.*

class ComplicationsConfigActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complications_config)
        ComplicationsInfoRetriever.init(this)

        colorConfig.setOnClickListener { startActivityWithRevealAnimation(ColorPickerActivity.newIntent(this, Type.COMPLICATIONS)) }
        configCenter.setOnClickListener { openComplicationConfig(Complications.CENTER) }
        configBottom.setOnClickListener { openComplicationConfig(Complications.BOTTOM) }
    }

    override fun onResume() {
        super.onResume()
        updateComplicationsInfo()
    }

    private fun updateComplicationsInfo() {
        configCenterImage.setImageResource(R.drawable.ic_plus)
        configCenterText.text = getString(R.string.config_center)
        configBottomImage.setImageResource(R.drawable.ic_plus)
        configBottomText.text = getString(R.string.config_bottom)

        ComplicationsInfoRetriever.retrieveInfo(this, { id, icon, name ->
            when (id) {
                Complications.CENTER -> {
                    configCenterImage.setImageIcon(icon)
                    configCenterText.text = name
                }
                Complications.BOTTOM -> {
                    configBottomImage.setImageIcon(icon)
                    configBottomText.text = name
                }
            }
        })
    }

    private fun openComplicationConfig(id: Int) {
        Complications.SUPPORTED_TYPES[id]?.let {
            startActivityWithRevealAnimation(ComplicationHelperActivity.createProviderChooserHelperIntent(this, getWatchFaceServiceComponentName(), id, *it))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ComplicationsInfoRetriever.release()
    }
}
package com.markantoni.linies.app.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.wearable.companion.WatchFaceCompanion
import androidx.appcompat.app.AppCompatActivity
import com.markantoni.linies.app.R
import com.markantoni.linies.common.util.logd

class ConfigurationActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, nodeId: String) {
            context.startActivity(Intent(context, ConfigurationActivity::class.java).apply {
                putExtra(WatchFaceCompanion.EXTRA_PEER_ID, nodeId)
            })
        }
    }

    private val nodeId by lazy {
        intent.getStringExtra(WatchFaceCompanion.EXTRA_PEER_ID) ?: error("Need to pass nodeId")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        logd("Configuring $nodeId")
    }
}
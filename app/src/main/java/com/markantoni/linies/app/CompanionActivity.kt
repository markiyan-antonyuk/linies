package com.markantoni.linies.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.wearable.Wearable
import com.markantoni.linies.common.data.DataProtocol
import com.markantoni.linies.common.data.DataReceiver
import com.markantoni.linies.common.data.DataSender
import com.markantoni.linies.common.util.logd

class CompanionActivity : AppCompatActivity() {
    private lateinit var viewModel: CompanionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        viewModel = ViewModelProviders.of(this).get(CompanionViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        val dataReceiver = DataReceiver(application)
        dataReceiver.listenData(DataProtocol.COMPANION) {
            logd("received")
        }
//
        dataReceiver.listenMessages(DataProtocol.COMPANION) {
            logd("xyi $it")
        }
//
        Wearable.getDataClient(this).addListener {
            logd("DFGDFGFDGDFG")
        }

        viewModel.requestConfiguration()
    }
}
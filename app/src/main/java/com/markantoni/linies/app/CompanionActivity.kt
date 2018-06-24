package com.markantoni.linies.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.markantoni.linies.common.util.setOnCheckedChangedListener
import kotlinx.android.synthetic.main.activity_configuration.*

class CompanionActivity : AppCompatActivity() {
    private lateinit var viewModel: CompanionViewModel
//    private val engine get() = watchfaceView.engine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        viewModel = ViewModelProviders.of(this).get(CompanionViewModel::class.java)
        viewModel.apply {
            configuration.observe(this@CompanionActivity, Observer {
//                it?.let { watchfaceView.engine.updateConfiguration(it) }
            })
        }

//        ambientSwitch.setOnCheckedChangedListener {
//            engine.apply {
//                isInAmbientMode = it
//                onAmbientModeChanged(it)
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.requestConfiguration()
    }
}
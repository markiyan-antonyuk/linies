package com.markantoni.linies.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_configuration.*

class CompanionActivity : AppCompatActivity() {
    private lateinit var viewModel: CompanionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        viewModel = ViewModelProviders.of(this).get(CompanionViewModel::class.java)
        viewModel.apply {
            configuration.observe(this@CompanionActivity, Observer {
                it?.let { watchfaceView.engine.updateConfiguration(it) }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.requestConfiguration()
    }
}
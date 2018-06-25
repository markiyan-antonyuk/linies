package com.markantoni.linies.app.ui.configuration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.markantoni.linies.app.R

class ConfigurationActivity : AppCompatActivity() {
    private lateinit var mViewModel: ConfigurationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        mViewModel = ViewModelProviders.of(this).get(ConfigurationViewModel::class.java)
        mViewModel.apply {
            configuration.observe(this@ConfigurationActivity, Observer {
            })
        }
    }

    override fun onStart() {
        super.onStart()
        mViewModel.requestConfiguration()
    }
}
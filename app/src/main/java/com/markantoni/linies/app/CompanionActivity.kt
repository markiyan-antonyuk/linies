package com.markantoni.linies.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class CompanionActivity : AppCompatActivity() {
    private lateinit var viewModel: CompanionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        viewModel = ViewModelProviders.of(this).get(CompanionViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        viewModel.requestConfiguration()
    }
}
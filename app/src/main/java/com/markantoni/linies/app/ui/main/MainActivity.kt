package com.markantoni.linies.app.ui.main

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markantoni.linies.app.R
import com.markantoni.linies.app.animateToSet
import com.markantoni.linies.app.observe
import com.markantoni.linies.app.viewModel
import com.markantoni.linies.common.util.DeviceExplorer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.apply {
            observe(isLoading) {
                if (it) mainRoot.animateToSet(R.layout.activity_main)
            }

            observe(devicesFound) {
                if (it) Unit
                else mainRoot.animateToSet(R.layout.activity_main_explore)
            }
        }

        mainRetryBtn.setOnClickListener { viewModel.searchForDevices() }
    }

    override fun onStart() {
        super.onStart()
        viewModel.searchForDevices()
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val isLoading = MutableLiveData<Boolean>()
    val devicesFound = MutableLiveData<Boolean>()

    fun searchForDevices() = launch(UI) {
        isLoading.value = true
        val devices = DeviceExplorer.findDevices(getApplication())
        devicesFound.value = devices.isNotEmpty()
        isLoading.value = false
    }
}
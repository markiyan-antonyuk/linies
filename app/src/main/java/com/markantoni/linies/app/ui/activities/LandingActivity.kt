package com.markantoni.linies.app.ui.activities

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.wearable.Node
import com.markantoni.linies.app.R
import com.markantoni.linies.app.animateToSet
import com.markantoni.linies.app.observe
import com.markantoni.linies.app.viewModel
import com.markantoni.linies.common.util.DeviceExplorer
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class LandingActivity : AppCompatActivity() {
    private val viewModel by viewModel<LandingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        viewModel.apply {
            observe(isLoading) {
                if (it) landingRoot.animateToSet(R.layout.activity_landing)
            }

            observe(devices) {
                if (it.isEmpty()) {
                    landingRoot.animateToSet(R.layout.activity_landing_explore)
                } else {
                    //todo maybe show chooser dialog?
                    ConfigurationActivity.startActivity(this@LandingActivity, it[0].id)
                }
            }
        }

        landingRetryBtn.setOnClickListener { viewModel.searchForDevices() }
    }

    override fun onStart() {
        super.onStart()
        viewModel.searchForDevices()
    }
}

class LandingViewModel(application: Application) : AndroidViewModel(application) {
    val isLoading = MutableLiveData<Boolean>()
    val devices = MutableLiveData<List<Node>>()

    fun searchForDevices() = launch(UI) {
        isLoading.value = true
        devices.value = DeviceExplorer.findDevices(getApplication(), true)
        isLoading.value = false
    }
}
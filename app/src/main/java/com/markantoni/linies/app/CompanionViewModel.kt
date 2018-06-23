package com.markantoni.linies.app

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markantoni.linies.common.data.DataReceiver
import com.markantoni.linies.common.data.DataSender
import com.markantoni.linies.common.util.logd

class CompanionViewModel(application: Application) : AndroidViewModel(application) {
    private val dataReceiver = DataReceiver(application, onDataListener = ::onDataReceived)

    val isLoading = MutableLiveData<Boolean>()

    init {
//        dataReceiver.connect()
    }

    override fun onCleared() {
        super.onCleared()
//        dataReceiver.disconnect()
    }

    fun requestConfiguration() {
        isLoading.value = true
        DataSender(getApplication()).sendRequestData()
    }

    private fun onDataReceived(bundle: Bundle) {
        isLoading.value = false
        logd(bundle)
    }
}
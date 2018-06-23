package com.markantoni.linies.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markantoni.linies.common.data.DataReceiver
import com.markantoni.linies.common.data.DataSender
import com.markantoni.linies.common.data.DataTransfer
import com.markantoni.linies.common.util.logd

class CompanionViewModel(application: Application) : AndroidViewModel(application) {
    private val configReceiver = DataReceiver(application)

    val isLoading = MutableLiveData<Boolean>()

    init {
        configReceiver.listen(DataTransfer.MessageType.Config(DataTransfer.Protocol.REMOTE), config = {
            isLoading.value = false
            logd(it)
        })
    }

    override fun onCleared() {
        super.onCleared()
        configReceiver.disconnect()
    }

    fun requestConfiguration() {
        isLoading.value = true
        DataSender(getApplication()).send(DataTransfer.Protocol.REMOTE, DataTransfer.MESSAGE_REQUEST_CONFIGURATION)
    }
}
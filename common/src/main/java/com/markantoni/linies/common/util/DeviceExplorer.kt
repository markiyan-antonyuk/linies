package com.markantoni.linies.common.util

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import java.util.concurrent.TimeUnit

object DeviceExplorer {
    suspend fun findDevices(context: Context): List<String> = withContext(CommonPool) {
        try {
            Tasks.await(Wearable.getNodeClient(context).connectedNodes, 2, TimeUnit.SECONDS).map { it.id }
        } catch (e: Exception) {
            Thread.sleep(1000)
            emptyList<String>()
        }
    }
}
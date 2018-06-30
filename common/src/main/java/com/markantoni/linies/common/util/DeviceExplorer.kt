package com.markantoni.linies.common.util

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import java.util.concurrent.TimeUnit

object DeviceExplorer {
    private const val DEBOUNCE_TIME_MS = 1000L

    suspend fun findDevices(context: Context, debounce: Boolean = false): List<Node> = withContext(CommonPool) {
        try {
            Tasks.await(Wearable.getNodeClient(context).connectedNodes, 2, TimeUnit.SECONDS).also {
                if (it.isEmpty() && debounce) Thread.sleep(DEBOUNCE_TIME_MS)
            }
        } catch (e: Exception) {
            if (debounce) Thread.sleep(DEBOUNCE_TIME_MS)
            emptyList<Node>()
        }
    }
}
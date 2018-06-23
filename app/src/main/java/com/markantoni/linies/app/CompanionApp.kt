package com.markantoni.linies.app

import android.app.Application
import com.markantoni.linies.common.util.Logger

class CompanionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.init("Phone")
    }
}
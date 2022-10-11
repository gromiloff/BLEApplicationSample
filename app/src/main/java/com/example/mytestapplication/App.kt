package com.example.mytestapplication

import android.app.Application
import com.ido.ble.BLEManager
import com.ido.ble.InitParam

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        BLEManager.onApplicationCreate(this)
        BLEManager.init()
    }
}
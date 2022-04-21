package com.example.doitsoon

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LaunchesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
        }
    }
}
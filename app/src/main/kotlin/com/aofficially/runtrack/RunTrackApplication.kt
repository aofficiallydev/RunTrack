package com.aofficially.runtrack

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RunTrackApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initial()
    }

    private fun initial() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}
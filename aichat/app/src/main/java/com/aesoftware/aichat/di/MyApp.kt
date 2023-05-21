package com.aesoftware.aichat.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        /*
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(listOf(viewModelModule, repositoryModule))
        }
         */
    }
}
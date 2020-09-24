package com.stambulo.kotlinnotes.ui

import android.app.Application
import org.koin.android.ext.android.startKoin
import com.stambulo.kotlinnotes.di.appModule
import com.stambulo.kotlinnotes.di.mainModule
import com.stambulo.kotlinnotes.di.noteModule
import com.stambulo.kotlinnotes.di.splashModule

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}


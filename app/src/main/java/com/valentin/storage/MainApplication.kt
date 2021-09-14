package com.valentin.storage

import android.app.Application
import android.content.Context
import com.valentin.storage.di.AppComponent
import com.valentin.storage.di.DaggerAppComponent

class MainApplication: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when(this) {
        is MainApplication -> appComponent
        else -> this.applicationContext.appComponent
    }
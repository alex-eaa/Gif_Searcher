package com.elchaninov.gif_searcher

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.elchaninov.gif_searcher.di.ApplicationComponent
import com.elchaninov.gif_searcher.di.DaggerApplicationComponent


class App : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerApplicationComponent.builder().setContext(this).build()

        Settings(this).nightTheme?.let { AppCompatDelegate.setDefaultNightMode(it.value) }
    }

    companion object {
        lateinit var instance: App
    }
}
package com.elchaninov.gif_searcher

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.elchaninov.gif_searcher.model.datasource.Settings
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var settings: Settings

    override fun onCreate() {
        super.onCreate()
        settings.nightTheme?.let { AppCompatDelegate.setDefaultNightMode(it.value) }
    }
}
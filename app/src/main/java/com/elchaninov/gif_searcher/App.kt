package com.elchaninov.gif_searcher

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.elchaninov.gif_searcher.model.datasource.AppSettings
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var settings: AppSettings

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(settings.nightTheme.value)
    }
}
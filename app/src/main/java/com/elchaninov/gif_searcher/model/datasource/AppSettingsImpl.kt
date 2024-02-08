package com.elchaninov.gif_searcher.model.datasource

import android.content.Context
import com.elchaninov.gif_searcher.base.Preference
import com.elchaninov.gif_searcher.myEnum.Layout
import com.elchaninov.gif_searcher.myEnum.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class AppSettingsImpl @Inject constructor(@ApplicationContext appContext: Context) : AppSettings {

    private val sPref = appContext.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )

    override var layoutManager: Layout
        get() = Preference.EnumPreference(sPref, KEY_LAYOUT, Layout::class.java, Layout.STAGGERED)
            .get()
        set(value) {
            Preference
                .EnumPreference(sPref, KEY_LAYOUT, Layout::class.java, Layout.STAGGERED)
                .set(value)
        }

    override var nightTheme: Theme
        get() = Preference.EnumPreference(sPref, KEY_THEME, Theme::class.java, Theme.AUTO).get()
        set(value) {
            Preference
                .EnumPreference(sPref, KEY_THEME, Theme::class.java, Theme.AUTO)
                .set(value)
        }

    override fun observeNightTheme(): Flow<Theme> = Preference
        .EnumPreference(sPref, KEY_THEME, Theme::class.java, Theme.AUTO)
        .getAsFlow()
        .map {
            it ?: Theme.AUTO
        }

    companion object {
        private const val PREF_NAME = "settings"
        private const val KEY_LAYOUT = "KEY_LAYOUT_SETTINGS"
        private const val KEY_THEME = "KEY_THEME_SETTINGS"
    }
}
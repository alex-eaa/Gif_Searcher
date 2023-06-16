package com.elchaninov.gif_searcher.model.datasource

import android.content.Context
import android.content.SharedPreferences
import com.elchaninov.gif_searcher.enum.Layout
import com.elchaninov.gif_searcher.enum.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Singleton
class AppSettingsImpl @Inject constructor(@ApplicationContext context: Context) : AppSettings {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override var layoutManager by prefs.layout(
        defaultValue = Layout.STAGGERED
    )

    override var nightTheme by prefs.theme(
        defaultValue = Theme.AUTO
    )

    private fun SharedPreferences.theme(
        defaultValue: Theme,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, Theme> =
        object : ReadWriteProperty<Any, Theme> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = Theme.fromName(getString(key(property), defaultValue.name)) ?: defaultValue

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: Theme
            ) = edit().putString(key(property), value.name).apply()
        }

    private fun SharedPreferences.layout(
        defaultValue: Layout,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, Layout> =
        object : ReadWriteProperty<Any, Layout> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = Layout.fromName(getString(key(property), defaultValue.name)) ?: defaultValue

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: Layout
            ) = edit().putString(key(property), value.name).apply()
        }

    companion object {
        const val PREF_NAME = "settings"
    }
}
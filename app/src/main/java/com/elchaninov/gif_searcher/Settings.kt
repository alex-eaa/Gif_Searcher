package com.elchaninov.gif_searcher

import android.content.Context
import android.content.SharedPreferences
import com.elchaninov.gif_searcher.ui.enum.Layout
import com.elchaninov.gif_searcher.ui.enum.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Singleton
class Settings @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var layoutManager by prefs.layout(
        defaultValue = Layout.STAGGERED
    )

    var nightTheme by prefs.theme(
        defaultValue = Theme.AUTO
    )

    private fun SharedPreferences.theme(
        defaultValue: Theme,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, Theme?> =
        object : ReadWriteProperty<Any, Theme?> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = if (getString(key(property), defaultValue.name) != null) {
                Theme.fromName(getString(key(property), defaultValue.name))
            } else defaultValue

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: Theme?
            ) = edit().putString(key(property), value?.name).apply()
        }

    private fun SharedPreferences.layout(
        defaultValue: Layout,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, Layout?> =
        object : ReadWriteProperty<Any, Layout?> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = if (getString(key(property), defaultValue.name) != null) {
                Layout.fromName(getString(key(property), defaultValue.name))
            } else defaultValue

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: Layout?
            ) = edit().putString(key(property), value?.name).apply()
        }

    companion object {
        const val PREF_NAME = "settings"
    }
}
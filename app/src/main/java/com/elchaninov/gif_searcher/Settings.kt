package com.elchaninov.gif_searcher

import android.content.Context
import android.content.SharedPreferences
import com.elchaninov.gif_searcher.ui.Theme
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Singleton
class Settings @Inject constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var isLinearLayoutManager by prefs.boolean()
    var isDarkTheme by prefs.string(
        defaultValue = Theme.AUTO.value
    )

     private fun SharedPreferences.boolean(
        defaultValue: Boolean = false,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, Boolean> =
        object : ReadWriteProperty<Any, Boolean> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = getBoolean(key(property), defaultValue)

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: Boolean
            ) = edit().putBoolean(key(property), value).apply()
        }

    private fun SharedPreferences.string(
        defaultValue: String? = null,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, String?> =
        object : ReadWriteProperty<Any, String?> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = getString(key(property), defaultValue)

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: String?
            ) = edit().putString(key(property), value).apply()
        }

    companion object {
        const val PREF_NAME = "settings"
    }
}
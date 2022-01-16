package com.elchaninov.gif_searcher

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Settings(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var isLinearLayoutManager by prefs.boolean()

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

    companion object {
        const val PREF_NAME = "settings"
    }
}
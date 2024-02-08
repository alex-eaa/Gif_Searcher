package com.elchaninov.gif_searcher.base

import android.content.SharedPreferences
import androidx.core.content.edit
import java.lang.Enum.valueOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

sealed class Preference<T>(
    private val sharedPreferences: SharedPreferences, private val key: String
) {
    abstract fun get(): T?
    abstract fun set(value: T?)

    @ExperimentalCoroutinesApi
    private val preferenceFlow = callbackFlow {
        trySend(key)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> trySend(key) }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    @ExperimentalCoroutinesApi
    fun getAsFlow() = preferenceFlow
        .filter { it == key }
        .map { get() }

    class StringPreference(
        private val sharedPreferences: SharedPreferences,
        private val key: String,
        private val defaultValue: String? = null
    ) : Preference<String>(sharedPreferences, key) {
        override fun get() = sharedPreferences.getString(key, defaultValue)
        override fun set(value: String?) = sharedPreferences.edit { putString(key, value) }
    }

    class IntPreference(
        private val sharedPreferences: SharedPreferences, private val key: String, private val defaultValue: Int
    ) : Preference<Int>(sharedPreferences, key) {
        override fun get() = sharedPreferences.getInt(key, defaultValue)
        override fun set(value: Int?) = sharedPreferences.edit { value?.let { putInt(key, it) } ?: run { remove(key) } }
    }

    class BooleanPreference(
        private val sharedPreferences: SharedPreferences,
        private val key: String,
        private val defaultValue: Boolean = false
    ) : Preference<Boolean>(sharedPreferences, key) {
        override fun get() = sharedPreferences.getBoolean(key, defaultValue)
        override fun set(value: Boolean?) =
            sharedPreferences.edit { value?.let { putBoolean(key, it) } ?: run { remove(key) } }
    }

    class NullableBooleanPreference(
        private val sharedPreferences: SharedPreferences, private val key: String
    ) : Preference<Boolean>(sharedPreferences, key) {
        override fun get() = if (sharedPreferences.contains(key)) sharedPreferences.getBoolean(
            key, false
        ) else null

        override fun set(value: Boolean?) =
            sharedPreferences.edit { value?.let { putBoolean(key, it) } ?: run { remove(key) } }
    }

    class FloatPreference(
        private val sharedPreferences: SharedPreferences, private val key: String, private val defaultValue: Float
    ) : Preference<Float>(sharedPreferences, key) {
        override fun get() = sharedPreferences.getFloat(key, defaultValue)
        override fun set(value: Float?) =
            sharedPreferences.edit { value?.let { putFloat(key, it) } ?: run { remove(key) } }
    }

    class LongPreference(
        private val sharedPreferences: SharedPreferences, private val key: String, private val defaultValue: Long
    ) : Preference<Long>(sharedPreferences, key) {
        override fun get() = sharedPreferences.getLong(key, defaultValue)
        override fun set(value: Long?) =
            sharedPreferences.edit { value?.let { putLong(key, it) } ?: run { remove(key) } }
    }

    class EnumPreference<ENUM_TYPE : Enum<ENUM_TYPE>>(
        private val sharedPreferences: SharedPreferences,
        private val key: String,
        private val enumClass: Class<ENUM_TYPE>,
        private val defaultValue: ENUM_TYPE
    ) : Preference<ENUM_TYPE>(sharedPreferences, key) {
        override fun get() = sharedPreferences
            .getString(key, null)
            ?.let { serializedEnum -> valueOf(enumClass, serializedEnum) } ?: defaultValue

        override fun set(value: ENUM_TYPE?) = sharedPreferences.edit { putString(key, value?.name) }
    }

    class StringSetSharePreference(
        private val sharedPreferences: SharedPreferences,
        private val key: String,
        private val defaultValue: Set<String> = hashSetOf()
    ) : Preference<Set<String>>(sharedPreferences, key) {
        override fun get(): Set<String> = sharedPreferences.getStringSet(key, defaultValue) ?: defaultValue

        override fun set(value: Set<String>?) = sharedPreferences.edit { putStringSet(key, value) }
    }

    class ObjectPreference<OBJECT_TYPE>(
        private val sharedPreferences: SharedPreferences,
        private val key: String,
        private val serialize: (OBJECT_TYPE) -> String?,
        private val deserialize: (String) -> OBJECT_TYPE?,
        private val defaultValue: OBJECT_TYPE?
    ) : Preference<OBJECT_TYPE>(sharedPreferences, key) {
        override fun get() = sharedPreferences
            .getString(key, null)
            ?.let { serializedObject -> deserialize(serializedObject) } ?: defaultValue

        override fun set(value: OBJECT_TYPE?) = sharedPreferences.edit { putString(key, value?.let(serialize)) }
    }
}
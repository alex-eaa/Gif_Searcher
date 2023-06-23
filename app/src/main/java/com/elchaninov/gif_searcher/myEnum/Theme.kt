package com.elchaninov.gif_searcher.myEnum

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

enum class Theme(val value: Int) {
    LIGHT(MODE_NIGHT_NO),
    DARK(MODE_NIGHT_YES),
    AUTO(MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        @JvmStatic
        fun fromName(name: String?): Theme? =
            values().find { value -> value.name == name }
    }
}
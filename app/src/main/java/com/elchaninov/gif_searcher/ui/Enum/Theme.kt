package com.elchaninov.gif_searcher.ui.Enum

enum class Theme {
    LIGHT,
    DARK,
    AUTO;

    companion object {
        @JvmStatic
        fun fromName(status: String?): Theme? =
            values().find { value -> value.name == status }
    }
}
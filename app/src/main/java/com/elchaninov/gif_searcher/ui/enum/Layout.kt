package com.elchaninov.gif_searcher.ui.enum

enum class Layout {
    LINEAR,
    STAGGERED;

    companion object {
        @JvmStatic
        fun fromName(name: String?): Layout? =
            values().find { value -> value.name == name }
    }
}


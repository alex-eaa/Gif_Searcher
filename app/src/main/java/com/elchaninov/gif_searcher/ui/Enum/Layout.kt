package com.elchaninov.gif_searcher.ui.Enum

enum class Layout {
    LINEAR,
    GRID,
    STAGGERED;

    companion object {
        @JvmStatic
        fun fromName(name: String?): Layout? =
            values().find { value -> value.name == name }
    }
}


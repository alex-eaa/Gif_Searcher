package com.elchaninov.gif_searcher.ui.Enum

enum class Layout {
    LINEAR,
    GRID,
    STAGGERED;

    companion object {
        @JvmStatic
        fun fromName(status: String?): Layout? =
            values().find { value -> value.name == status }
    }
}


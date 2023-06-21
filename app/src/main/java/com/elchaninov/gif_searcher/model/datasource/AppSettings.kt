package com.elchaninov.gif_searcher.model.datasource

import com.elchaninov.gif_searcher.enum.Layout
import com.elchaninov.gif_searcher.enum.Theme

interface AppSettings {

    var layoutManager: Layout
    var nightTheme: Theme
}
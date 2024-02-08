package com.elchaninov.gif_searcher.model.datasource

import com.elchaninov.gif_searcher.myEnum.Layout
import com.elchaninov.gif_searcher.myEnum.Theme
import kotlinx.coroutines.flow.Flow

interface AppSettings {

    var layoutManager: Layout
    var nightTheme: Theme

    fun observeNightTheme(): Flow<Theme>
}
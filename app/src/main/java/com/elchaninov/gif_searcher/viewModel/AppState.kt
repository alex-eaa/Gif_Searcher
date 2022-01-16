package com.elchaninov.gif_searcher.viewModel

import com.elchaninov.gif_searcher.data.Gif

sealed class AppState{
    data class Success(val data: List<Gif>) : AppState()
    data class Error(val message: String) : AppState()
    object Loading : AppState()
}

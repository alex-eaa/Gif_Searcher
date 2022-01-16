package com.elchaninov.gif_searcher.viewModel

sealed class AppState{
    data class Error(val message: String) : AppState()
    object Loading : AppState()
}

package com.elchaninov.gif_searcher.viewModel

sealed class LoadingState<T> {
    data class Success<T>(val file: T) : LoadingState<T>()
    data class Failure<T>(val throwable: Throwable) : LoadingState<T>()
    data class Progress<T>(val percent: Int = 0) : LoadingState<T>()
}
package com.elchaninov.gif_searcher.viewModel

sealed class LoadingState<T> {
    data class Success<T>(val data: T) : LoadingState<T>()
    data class Failure<T>(val throwable: Throwable, val data: T? = null) : LoadingState<T>()
    data class Progress<T>(val percent: Int = 0, val data: T? = null) : LoadingState<T>()
}
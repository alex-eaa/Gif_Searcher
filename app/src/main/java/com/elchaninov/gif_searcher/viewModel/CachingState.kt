package com.elchaninov.gif_searcher.viewModel

import java.io.File

sealed class CachingState {
    data class Success(val file: File) : CachingState()
    data class Failure(val throwable: Throwable) : CachingState()
}
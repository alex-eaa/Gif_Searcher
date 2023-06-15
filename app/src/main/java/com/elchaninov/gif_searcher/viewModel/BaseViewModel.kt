package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel(
    val giphyGifsRepository: GiphyGifsRepository,
) : ViewModel() {

    var isFavoritesNotEmpty: Boolean = false
        private set

    val isFavoritesNotEmptyFlow: Flow<Boolean> = giphyGifsRepository.isFavoritesNotEmpty()
        .onEach {
            isFavoritesNotEmpty = it
        }
}

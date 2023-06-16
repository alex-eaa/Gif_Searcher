package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.data.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel(
    favoritesRepository: FavoritesRepository,
) : ViewModel() {

    var isFavoritesNotEmpty: Boolean = false
        private set

    val isFavoritesNotEmptyFlow: Flow<Boolean> = favoritesRepository.isFavoritesNotEmpty()
        .onEach {
            isFavoritesNotEmpty = it
        }
}

package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    giphyGifsRepository: GiphyGifsRepository,
) : ViewModel() {
    val favoritesFlow = giphyGifsRepository.getFavoritesFlow()
}
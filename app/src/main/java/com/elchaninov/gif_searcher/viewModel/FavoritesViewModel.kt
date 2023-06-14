package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    giphyGifsRepository: GiphyGifsRepository,
) : ViewModel() {
    val favoritesFlow = giphyGifsRepository.getFavoritesFlow()
}
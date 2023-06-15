package com.elchaninov.gif_searcher.viewModel

import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.model.Gif
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    giphyGifsRepository: GiphyGifsRepository,
) : BaseViewModel(giphyGifsRepository) {

    val favoritesFlow: Flow<List<Gif>> = giphyGifsRepository.getFavoritesFlow()
}
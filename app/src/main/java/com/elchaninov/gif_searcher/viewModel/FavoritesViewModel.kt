package com.elchaninov.gif_searcher.viewModel

import com.elchaninov.gif_searcher.model.data.userdata.Gif
import com.elchaninov.gif_searcher.model.datasource.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoritesRepository: FavoritesRepository,
) : BaseViewModel(favoritesRepository) {

    val favoritesFlow: Flow<List<Gif>> = favoritesRepository.getFavoritesFlow()
}
package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.model.datasource.AppSettings
import com.elchaninov.gif_searcher.model.datasource.FavoritesRepository
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel(
    favoritesRepository: FavoritesRepository,
    appSettings: AppSettings,
) : ViewModel() {

    val isFavoritesNotEmptyFlow: Flow<Boolean> = favoritesRepository.isFavoritesNotEmpty()
    val nightThemeFlow = appSettings.observeNightTheme()
}

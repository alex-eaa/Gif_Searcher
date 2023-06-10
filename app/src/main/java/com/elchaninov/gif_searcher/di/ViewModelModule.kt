package com.elchaninov.gif_searcher.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elchaninov.gif_searcher.viewModel.CategoriesViewModel
import com.elchaninov.gif_searcher.viewModel.FavoritesViewModel
import com.elchaninov.gif_searcher.viewModel.FullGifViewModel
import com.elchaninov.gif_searcher.viewModel.GifsViewModel
import com.elchaninov.gif_searcher.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GifsViewModel::class)
    protected abstract fun gifsViewModel(gifsViewModel: GifsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullGifViewModel::class)
    protected abstract fun fullGifViewModel(fullGifViewModel: FullGifViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    protected abstract fun categoriesViewModel(categoriesViewModel: CategoriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    protected abstract fun favoritesViewModel(favoritesViewModel: FavoritesViewModel): ViewModel
}
package com.elchaninov.gif_searcher.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elchaninov.gif_searcher.viewModel.MainViewModel
import com.elchaninov.gif_searcher.viewModel.ShowingGifViewModel
import com.elchaninov.gif_searcher.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    protected abstract fun mainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShowingGifViewModel::class)
    protected abstract fun showingGifViewModel(showingGifViewModel: ShowingGifViewModel): ViewModel
}
package com.elchaninov.gif_searcher.di

import com.elchaninov.gif_searcher.data.FavoritesRepository
import com.elchaninov.gif_searcher.data.FavoritesRepositoryImpl
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.data.GiphyGifsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindGiphyGifsRepository(giphyGifsRepositoryImpl: GiphyGifsRepositoryImpl): GiphyGifsRepository

    @Binds
    abstract fun bindFavoritesRepository(favoritesRepositoryImpl: FavoritesRepositoryImpl): FavoritesRepository
}
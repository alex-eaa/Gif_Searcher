package com.elchaninov.gif_searcher.di

import com.elchaninov.gif_searcher.model.datasource.FavoritesRepository
import com.elchaninov.gif_searcher.model.datasource.FavoritesRepositoryImpl
import com.elchaninov.gif_searcher.model.datasource.GiphyGifsRepository
import com.elchaninov.gif_searcher.model.datasource.GiphyGifsRepositoryImpl
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
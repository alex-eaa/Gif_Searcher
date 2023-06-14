package com.elchaninov.gif_searcher.di

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
    abstract fun bindsGiphyGifsRepository(giphyGifsRepositoryImpl: GiphyGifsRepositoryImpl): GiphyGifsRepository
}
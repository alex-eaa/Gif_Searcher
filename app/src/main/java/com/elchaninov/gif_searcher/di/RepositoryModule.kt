package com.elchaninov.gif_searcher.di

import com.elchaninov.gif_searcher.data.GetGifsRxRepository
import com.elchaninov.gif_searcher.data.GetGifsRxRepositoryImpl
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.data.GiphyGifsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsGiphyGifsRepository(giphyGifsRepositoryImpl: GiphyGifsRepositoryImpl): GiphyGifsRepository

    @Binds
    abstract fun bindsGetGIfsRxRepository(getGifsRxRepositoryImpl: GetGifsRxRepositoryImpl): GetGifsRxRepository
}
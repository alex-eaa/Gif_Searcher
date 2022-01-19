package com.elchaninov.gif_searcher.di

import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.data.GiphyGifsRepositoryImpl
import com.elchaninov.gif_searcher.data.api.GiphyGifsApi
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideGiphyGifRepository(
        giphyGifsApi: GiphyGifsApi,
    ): GiphyGifsRepository {
        return GiphyGifsRepositoryImpl(giphyGifsApi)
    }
}
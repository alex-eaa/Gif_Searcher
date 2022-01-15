package com.elchaninov.gif_searcher.di

import com.elchaninov.gif_searcher.data.GiphyGifRepository
import com.elchaninov.gif_searcher.data.GiphyGifRepositoryImpl
import com.elchaninov.gif_searcher.data.mappers.MapGifDtoToGif
import com.elchaninov.gif_searcher.data.retrofit.GiphyApi
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideGiphyGifRepository(
        giphyApi: GiphyApi,
        mapper: MapGifDtoToGif,
    ): GiphyGifRepository {
        return GiphyGifRepositoryImpl(giphyApi, mapper)
    }
}
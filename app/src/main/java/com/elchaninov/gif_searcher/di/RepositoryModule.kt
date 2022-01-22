package com.elchaninov.gif_searcher.di

import androidx.paging.PagingConfig
import com.elchaninov.gif_searcher.data.*
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

    @Provides
    fun provideGetGIfsRxRepository(
        getGifsRxPagingSourceFactory: GifsRxPagingSource.Factory,
        pagingConfig: PagingConfig
    ): GetGifsRxRepository {
        return GetGifsRxRepositoryImpl(getGifsRxPagingSourceFactory, pagingConfig)
    }
}
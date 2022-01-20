package com.elchaninov.gif_searcher.di

import com.elchaninov.gif_searcher.data.GetGifsRxRepository
import com.elchaninov.gif_searcher.data.GetGifsRxRepositoryImpl
import com.elchaninov.gif_searcher.data.GifsRxPagingSource
import dagger.Module
import dagger.Provides

@Module
class GetGifsRxRepositoryModule {

    @Provides
    fun provideGetGIfsRxRepository(
        getGifsRxPagingSourceFactory: GifsRxPagingSource.Factory
    ): GetGifsRxRepository {
        return GetGifsRxRepositoryImpl(getGifsRxPagingSourceFactory)
    }
}
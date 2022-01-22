package com.elchaninov.gif_searcher.di

import androidx.paging.PagingConfig
import com.elchaninov.gif_searcher.data.GetGifsRxRepository
import com.elchaninov.gif_searcher.data.GetGifsRxRepositoryImpl
import com.elchaninov.gif_searcher.data.GifsRxPagingSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GetGifsRxRepositoryModule {

    @Singleton
    @Provides
    fun providePagingConfig(): PagingConfig = PagingConfig(
        pageSize = GifsRxPagingSource.PAGE_SIZE,
        enablePlaceholders = false,
    )

}
package com.elchaninov.gif_searcher.di

import androidx.paging.PagingConfig
import com.elchaninov.gif_searcher.model.datasource.GifsRxPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PagingModule {

    @Singleton
    @Provides
    fun providePagingConfig(): PagingConfig = PagingConfig(
        pageSize = GifsRxPagingSource.PAGE_SIZE,
        enablePlaceholders = false
    )
}
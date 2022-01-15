package com.elchaninov.gif_searcher.di

import com.elchaninov.gif_searcher.data.mappers.MapGifDtoToGif
import dagger.Module
import dagger.Provides

@Module
class MapperModule {

    @Provides
    fun provideMapper(): MapGifDtoToGif = MapGifDtoToGif()
}
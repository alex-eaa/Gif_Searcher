package com.elchaninov.gif_searcher.di

import android.content.Context
import com.elchaninov.gif_searcher.room.GifDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun database(@ApplicationContext appContext: Context): GifDatabase = GifDatabase.create(appContext)
}
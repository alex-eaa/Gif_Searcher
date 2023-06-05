package com.elchaninov.gif_searcher.di

import android.content.Context
import com.elchaninov.gif_searcher.room.GifDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    @Singleton
    fun database(appContext: Context): GifDatabase = GifDatabase.create(appContext)
}
package com.elchaninov.gif_searcher.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elchaninov.gif_searcher.BuildConfig

private const val DATABASE_NAME = "favorite_gif_db"

@Database(
    version = BuildConfig.DB_VERSION,
    entities = [GifEntity::class],
    exportSchema = true,
)

abstract class GifDatabase : RoomDatabase() {
    abstract val gifDao: GifDao

    companion object {
        fun create(context: Context): GifDatabase {
            return Room
                .databaseBuilder(context, GifDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
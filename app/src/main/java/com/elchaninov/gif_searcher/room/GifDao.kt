package com.elchaninov.gif_searcher.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GifDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GifEntity)

    @Delete
    suspend fun delete(entity: GifEntity)

    @Query("SELECT * FROM ${GifEntity.TABLE_NAME} ORDER BY ${GifEntity.FAVORITE_CREATED} DESC")
    fun observeAll(): LiveData<List<GifEntity>>
}
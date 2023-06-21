package com.elchaninov.gif_searcher.model.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elchaninov.gif_searcher.model.datasource.room.entity.GifEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GifDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GifEntity)

    @Query("DELETE FROM ${GifEntity.TABLE_NAME} WHERE ${GifEntity.GIF_ID} = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM ${GifEntity.TABLE_NAME} WHERE ${GifEntity.GIF_ID} = :id)")
    fun isObjectExists(id: String): Flow<Boolean>

    @Query("SELECT * FROM ${GifEntity.TABLE_NAME} ORDER BY ${GifEntity.FAVORITE_CREATED} DESC")
    fun observeAll(): Flow<List<GifEntity>>

    @Query("SELECT COUNT(*) FROM ${GifEntity.TABLE_NAME}")
    fun getRowCount(): Flow<Long>
}
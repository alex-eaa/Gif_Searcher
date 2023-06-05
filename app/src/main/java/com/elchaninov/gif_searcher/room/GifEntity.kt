package com.elchaninov.gif_searcher.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = GifEntity.TABLE_NAME)
data class GifEntity(
    @PrimaryKey @ColumnInfo(name = GIF_ID) val id: String,
    val title: String?,
    val type: String?,
    val urlPreview: String?,
    val heightPreview: Int?,
    val widthPreview: Int?,
    val urlOriginal: String,
    val sizeOriginal: Long,
    @ColumnInfo(name = FAVORITE_CREATED) val dateCreated: Long,
) {
    companion object {
        const val TABLE_NAME = "gif"
        const val GIF_ID = "gif_id"
        const val FAVORITE_CREATED = "created"
    }
}

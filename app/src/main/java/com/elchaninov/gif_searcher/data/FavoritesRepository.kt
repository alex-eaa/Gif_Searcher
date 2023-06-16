package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.model.Gif
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun toggleGifFavorite(gif: Gif)
    fun isFavoriteGifFlow(id: String): Flow<Boolean>
    fun getFavoritesFlow(): Flow<List<Gif>>
    fun isFavoritesNotEmpty(): Flow<Boolean>
}
package com.elchaninov.gif_searcher.data

import androidx.room.withTransaction
import com.elchaninov.gif_searcher.data.mappers.MapGifToGifEntity
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.room.GifDatabase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val gifDatabase: GifDatabase,
    private val mapGifToGifEntity: MapGifToGifEntity,
) : FavoritesRepository {

    override suspend fun toggleGifFavorite(gif: Gif) {
        gifDatabase.withTransaction {
            gifDatabase.gifDao.isObjectExists(gif.id).firstOrNull()?.let {
                if (it) gifDatabase.gifDao.deleteById(gif.id)
                else gifDatabase.gifDao.insert(mapGifToGifEntity.map(gif))
            }
        }
    }

    override fun isFavoriteGifFlow(id: String): Flow<Boolean> {
        return gifDatabase.gifDao.isObjectExists(id)
            .distinctUntilChanged()
    }

    override fun getFavoritesFlow(): Flow<List<Gif>> {
        return gifDatabase.gifDao.observeAll()
            .distinctUntilChanged()
            .map {
                it.map { mapGifToGifEntity.map(it) }
            }
    }

    override fun isFavoritesNotEmpty(): Flow<Boolean> {
        return gifDatabase.gifDao.getRowCount()
            .distinctUntilChanged()
            .map {
                it > 0
            }
            .distinctUntilChanged()
    }
}

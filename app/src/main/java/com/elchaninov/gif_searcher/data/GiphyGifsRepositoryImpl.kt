package com.elchaninov.gif_searcher.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import androidx.room.withTransaction
import com.elchaninov.gif_searcher.data.api.GiphyGifsApi
import com.elchaninov.gif_searcher.data.api.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.data.mappers.MapCategoryDtoToCategory
import com.elchaninov.gif_searcher.data.mappers.MapGifToGifEntity
import com.elchaninov.gif_searcher.model.Category
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.room.GifDatabase
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull

class GiphyGifsRepositoryImpl @Inject constructor(
    private val giphyGifsApi: GiphyGifsApi,
    private val mapCategoryDtoToCategory: MapCategoryDtoToCategory,
    private val factory: GifsRxPagingSource.Factory,
    private val pagingConfig: PagingConfig,
    private val gifDatabase: GifDatabase,
    private val mapGifToGifEntity: MapGifToGifEntity,
) : GiphyGifsRepository {

    override fun getGifs(query: String, offset: Int): Single<GiphyGifsResponseDto> =
        giphyGifsApi.fetchGifs(q = query, offset = offset)

    override fun getGifs(searchQuery: SearchQuery): Observable<PagingData<Gif>> {
        val pagingSource = factory.create(searchQuery)

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { pagingSource }
        ).observable
    }

    override fun getGifsTrending(offset: Int): Single<GiphyGifsResponseDto> =
        giphyGifsApi.fetchGifsTrending(offset = offset)

    override suspend fun getCategories(): List<Category> {
        return giphyGifsApi.fetchCategories().body()?.data?.map {
            mapCategoryDtoToCategory.map(it)
        } ?: listOf()
    }

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

    override suspend fun getFavorite(): LiveData<Gif> {
        TODO("Not yet implemented")
    }
}

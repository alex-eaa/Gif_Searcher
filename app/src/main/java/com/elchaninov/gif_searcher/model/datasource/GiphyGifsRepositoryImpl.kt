package com.elchaninov.gif_searcher.model.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.elchaninov.gif_searcher.model.api.GiphyGifsApi
import com.elchaninov.gif_searcher.model.data.SearchQuery
import com.elchaninov.gif_searcher.model.data.dto.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.model.data.mappers.MapCategoryDtoToCategory
import com.elchaninov.gif_searcher.model.data.userdata.Gif
import com.elchaninov.gif_searcher.model.data.userdata.TypedCategory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiphyGifsRepositoryImpl @Inject constructor(
    private val giphyGifsApi: GiphyGifsApi,
    private val mapCategoryDtoToCategory: MapCategoryDtoToCategory,
    private val factory: GifsRxPagingSource.Factory,
    private val pagingConfig: PagingConfig,
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

    override suspend fun getCategories(): List<TypedCategory.Category> {
        return giphyGifsApi.fetchCategories().body()?.data?.map {
            mapCategoryDtoToCategory.map(it)
        } ?: emptyList()
    }
}

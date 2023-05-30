package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.data.api.GiphyGifsApi
import com.elchaninov.gif_searcher.data.api.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.data.mappers.MapCategoryDtoToCategory
import com.elchaninov.gif_searcher.model.Category
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GiphyGifsRepositoryImpl @Inject constructor(
    private val giphyGifsApi: GiphyGifsApi,
    private val mapCategoryDtoToCategory: MapCategoryDtoToCategory
) : GiphyGifsRepository {

    override fun getGifs(query: String, offset: Int): Single<GiphyGifsResponseDto> =
        giphyGifsApi.fetchGifs(q = query, offset = offset)

    override fun getGifsTrending(offset: Int): Single<GiphyGifsResponseDto> =
        giphyGifsApi.fetchGifsTrending(offset = offset)

    override suspend fun getCategories(): List<Category> {
        return giphyGifsApi.fetchCategories().body()?.data?.map {
            mapCategoryDtoToCategory.map(it)
        } ?: listOf()
    }
}

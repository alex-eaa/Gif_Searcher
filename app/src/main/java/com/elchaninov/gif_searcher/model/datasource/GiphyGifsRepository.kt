package com.elchaninov.gif_searcher.model.datasource

import androidx.paging.PagingData
import com.elchaninov.gif_searcher.model.data.SearchQuery
import com.elchaninov.gif_searcher.model.data.dto.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.model.data.userdata.Gif
import com.elchaninov.gif_searcher.model.data.userdata.TypedCategory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface GiphyGifsRepository {
    fun getGifs(query: String, offset: Int): Single<GiphyGifsResponseDto>
    fun getGifs(searchQuery: SearchQuery): Observable<PagingData<Gif>>
    fun getGifsTrending(offset: Int): Single<GiphyGifsResponseDto>
    suspend fun getCategories(): List<TypedCategory.Category>
}
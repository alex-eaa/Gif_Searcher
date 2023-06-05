package com.elchaninov.gif_searcher.data

import androidx.paging.PagingData
import com.elchaninov.gif_searcher.data.api.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.model.Category
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface GiphyGifsRepository {
    fun getGifs(query: String, offset: Int): Single<GiphyGifsResponseDto>
    fun getGifs(searchQuery: SearchQuery): Observable<PagingData<Gif>>
    fun getGifsTrending(offset: Int): Single<GiphyGifsResponseDto>
    suspend fun getCategories(): List<Category>
}
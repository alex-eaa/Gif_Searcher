package com.elchaninov.gif_searcher.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.elchaninov.gif_searcher.data.api.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.model.Category
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface GiphyGifsRepository {
    fun getGifs(query: String, offset: Int): Single<GiphyGifsResponseDto>
    fun getGifs(searchQuery: SearchQuery): Observable<PagingData<Gif>>
    fun getGifsTrending(offset: Int): Single<GiphyGifsResponseDto>
    suspend fun getCategories(): List<Category>

    suspend fun toggleGifFavorite(gif: Gif)
    fun isFavoriteGifFlow(id: String): Flow<Boolean>
    suspend fun getFavorite(): LiveData<Gif>
}
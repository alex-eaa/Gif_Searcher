package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.data.api.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.model.Category
import io.reactivex.rxjava3.core.Single

interface GiphyGifsRepository {
    fun getGifs(query: String, offset: Int): Single<GiphyGifsResponseDto>
    fun getGifsTrending(offset: Int): Single<GiphyGifsResponseDto>
    suspend fun getCategories(): List<Category>
}
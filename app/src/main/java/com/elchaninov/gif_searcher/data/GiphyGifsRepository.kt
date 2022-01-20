package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.data.api.GiphyGifsResponse
import io.reactivex.rxjava3.core.Single

interface GiphyGifsRepository {
    fun getGifs(query: String, offset: Int): Single<GiphyGifsResponse>
    fun getGifsTrending(offset: Int): Single<GiphyGifsResponse>
}
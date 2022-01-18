package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.data.retrofit.GifDto
import io.reactivex.rxjava3.core.Single

interface GiphyGifRepository {
    fun getGifs(query: String?, offset: Int): Single<GifDto>
}
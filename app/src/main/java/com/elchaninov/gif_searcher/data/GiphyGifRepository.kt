package com.elchaninov.gif_searcher.data

import io.reactivex.rxjava3.core.Single

interface GiphyGifRepository {
    fun getGifs(): Single<List<Gif>>
}
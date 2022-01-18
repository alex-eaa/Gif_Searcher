package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.data.retrofit.GifDto
import com.elchaninov.gif_searcher.data.retrofit.GiphyApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GiphyGifRepositoryImpl @Inject constructor(
    private val giphyApi: GiphyApi
) : GiphyGifRepository {

    override fun getGifs(query: String?, offset: Int): Single<GifDto> {
        return if (query == null)
            giphyApi.fetchGifsTrending(offset = offset)
        else
            giphyApi.fetchGifs(q = query, offset = offset)
    }
}

package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.data.api.GiphyGifsResponse
import com.elchaninov.gif_searcher.data.api.GiphyGifsApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GiphyGifsRepositoryImpl @Inject constructor(
    private val giphyGifsApi: GiphyGifsApi
) : GiphyGifsRepository {

    override fun getGifs(query: String?, offset: Int): Single<GiphyGifsResponse> {
        return if (query == null)
            giphyGifsApi.fetchGifsTrending(offset = offset)
        else
            giphyGifsApi.fetchGifs(q = query, offset = offset)
    }
}

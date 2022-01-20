package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.data.api.GiphyGifsApi
import com.elchaninov.gif_searcher.data.api.GiphyGifsResponseDto
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GiphyGifsRepositoryImpl @Inject constructor(
    private val giphyGifsApi: GiphyGifsApi,
) : GiphyGifsRepository {

    override fun getGifs(query: String, offset: Int): Single<GiphyGifsResponseDto> =
        giphyGifsApi.fetchGifs(q = query, offset = offset)

    override fun getGifsTrending(offset: Int): Single<GiphyGifsResponseDto> =
        giphyGifsApi.fetchGifsTrending(offset = offset)
}

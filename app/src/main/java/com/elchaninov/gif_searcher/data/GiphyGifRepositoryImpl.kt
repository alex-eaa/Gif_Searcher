package com.elchaninov.gif_searcher.data

import com.elchaninov.gif_searcher.data.mappers.MapGifDtoToGif
import com.elchaninov.gif_searcher.data.retrofit.GiphyApi
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GiphyGifRepositoryImpl
@Inject constructor(
    private val giphyApi: GiphyApi,
    private val mapper: MapGifDtoToGif
) : GiphyGifRepository {

    override fun getGifs(query: String): Single<List<Gif>> = giphyApi.fetchGifs(q = query)
        .flatMap {
            Single.just(mapper.map(it))
        }

    override fun getGifsTrending(): Single<List<Gif>> = giphyApi.fetchGifsTrending()
        .flatMap {
            Single.just(mapper.map(it))
        }
}

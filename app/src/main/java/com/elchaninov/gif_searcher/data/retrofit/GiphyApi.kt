package com.elchaninov.gif_searcher.data.retrofit

import com.elchaninov.gif_searcher.BuildConfig
import com.elchaninov.gif_searcher.viewModel.GifsRxPagingSource.Companion.PAGE_SIZE
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET("/v1/gifs/search")
    fun fetchGifs(
        @Query("api_key") api_key: String = BuildConfig.GIPHY_API_KEY,
        @Query("q") q: String = "",
        @Query("limit") limit: Int = PAGE_SIZE,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("offset") lang: String = "en",
    ): Single<GifDto>

    @GET("/v1/gifs/trending")
    fun fetchGifsTrending(
        @Query("api_key") api_key: String = BuildConfig.GIPHY_API_KEY,
        @Query("limit") limit: Int = PAGE_SIZE,
        @Query("offset") offset: Int = 0,
    ): Single<GifDto>
}
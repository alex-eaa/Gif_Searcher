package com.elchaninov.gif_searcher.data.retrofit

import com.elchaninov.gif_searcher.BuildConfig
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET("/v1/gifs/search")
    fun fetchGifs(
        @Query("api_key") api_key: String = BuildConfig.GIPHY_API_KEY,
        @Query("q") q: String = "mini",
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("offset") lang: String = "en",
    ): Single<GifDto>
}
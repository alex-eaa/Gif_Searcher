package com.elchaninov.gif_searcher.data

import androidx.paging.PagingData
import com.elchaninov.gif_searcher.data.retrofit.GifDto
import io.reactivex.rxjava3.core.Flowable

interface GetGifsRxRepository {
    fun getGifs(): Flowable<PagingData<Gif>>
}
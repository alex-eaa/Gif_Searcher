package com.elchaninov.gif_searcher.data

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable

interface GetGifsRxRepository {
    fun getGifs(query: String?): Flowable<PagingData<Gif>>
}
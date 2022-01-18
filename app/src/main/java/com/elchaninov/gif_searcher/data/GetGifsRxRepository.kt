package com.elchaninov.gif_searcher.data

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Observable

interface GetGifsRxRepository {
    fun getGifs(query: String?): Observable<PagingData<Gif>>
}
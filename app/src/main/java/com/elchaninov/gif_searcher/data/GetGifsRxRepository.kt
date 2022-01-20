package com.elchaninov.gif_searcher.data

import androidx.paging.PagingData
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import io.reactivex.rxjava3.core.Observable

interface GetGifsRxRepository {
    fun getGifs(searchQuery: SearchQuery): Observable<PagingData<Gif>>
}
package com.elchaninov.gif_searcher.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetGifsRxRepositoryImpl @Inject constructor(
    private val factory: GifsRxPagingSource.Factory,
    private val pagingConfig: PagingConfig
) : GetGifsRxRepository {

    override fun getGifs(searchQuery: SearchQuery): Observable<PagingData<Gif>> {
        val pagingSource = factory.create(searchQuery)

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { pagingSource }
        ).observable
    }
}
package com.elchaninov.gif_searcher.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.elchaninov.gif_searcher.data.GifsRxPagingSource.Companion.PAGE_SIZE
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetGifsRxRepositoryImpl @Inject constructor(
    private val factory: GifsRxPagingSource.Factory
) : GetGifsRxRepository {

    override fun getGifs(searchQuery: SearchQuery): Observable<PagingData<Gif>> {
        val pagingSource = factory.create(searchQuery)

        return Pager(
            config = getPageConfig(),
            pagingSourceFactory = { pagingSource }
        ).observable
    }

    private fun getPageConfig(): PagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
//        maxSize = PAGE_SIZE + 4 * PAGE_SIZE,
        prefetchDistance = PAGE_SIZE * 3,
        initialLoadSize = PAGE_SIZE * 3
    )
}
package com.elchaninov.gif_searcher.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import com.elchaninov.gif_searcher.viewModel.GifsRxPagingSource
import com.elchaninov.gif_searcher.viewModel.GifsRxPagingSource.Companion.PAGE_SIZE
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetGifsRxRepositoryImpl @Inject constructor(
    private val pagingSource: GifsRxPagingSource
) : GetGifsRxRepository {

    override fun getGifs(query: String?): Observable<PagingData<Gif>> {
        pagingSource.query = query

        return Pager(
            config = getPageConfig(),
            pagingSourceFactory = { pagingSource }
        ).observable
    }

    private fun getPageConfig(): PagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
        maxSize = PAGE_SIZE + 4 * PAGE_SIZE,
        prefetchDistance = PAGE_SIZE * 2,
        initialLoadSize = PAGE_SIZE * 2
    )

    private fun getDefaultPageConfig(): PagingConfig =
        PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)

}
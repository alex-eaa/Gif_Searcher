package com.elchaninov.gif_searcher.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.elchaninov.gif_searcher.viewModel.GifsRxPagingSource
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetGifsRxRepositoryImpl @Inject constructor(
    private val pagingSource: GifsRxPagingSource
) : GetGifsRxRepository {

    override fun getGifs(): Flowable<PagingData<Gif>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 50,
                prefetchDistance = 20,
                initialLoadSize = 30
            ),
            pagingSourceFactory = { pagingSource }
        ).flowable
    }
}
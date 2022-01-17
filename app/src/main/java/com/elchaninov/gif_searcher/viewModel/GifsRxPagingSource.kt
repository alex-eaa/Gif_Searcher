package com.elchaninov.gif_searcher.viewModel

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.elchaninov.gif_searcher.data.mappers.MapGifDtoToGif
import com.elchaninov.gif_searcher.data.retrofit.GifDto
import com.elchaninov.gif_searcher.data.retrofit.GiphyApi
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GifsRxPagingSource @Inject constructor(
    val giphyApi: GiphyApi,
    val query: String,
    val mapGifDtoToGif: MapGifDtoToGif
) : RxPagingSource<Int, GifDto.DataDto>() {

    val loadSize = 20

    override fun getRefreshKey(state: PagingState<Int, GifDto.DataDto>): Int? {
        return null
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, GifDto.DataDto>> {
        var nextPageNumber: Int? = params.key
        if (nextPageNumber == null) {
            nextPageNumber = 0
        }

        return giphyApi.fetchGifsTrending(offset = 0)
            .map {
                it
            }
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(gifDto: GifDto): LoadResult<Int, GifDto.DataDto> {
        val totalCount = gifDto.pagination.totalCount
        val count = gifDto.pagination.count
        val offset = gifDto.pagination.offset
        return LoadResult.Page(
            data = gifDto.data,
            prevKey = if (offset == 0) null else offset - count,
            nextKey = if (offset == totalCount) null else offset + count
        )
    }


}
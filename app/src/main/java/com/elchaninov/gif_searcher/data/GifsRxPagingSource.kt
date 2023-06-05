package com.elchaninov.gif_searcher.data

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.elchaninov.gif_searcher.data.api.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.data.mappers.MapGifDtoToGif
import com.elchaninov.gif_searcher.model.Gif
import com.elchaninov.gif_searcher.viewModel.SearchQuery
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class GifsRxPagingSource @AssistedInject constructor(
    @Assisted private val searchQuery: SearchQuery,
    private val giphyGifsRepository: GiphyGifsRepository,
    private val mapGifDtoToGif: MapGifDtoToGif,
) : RxPagingSource<Int, Gif>() {

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Gif>> {
        val position = params.key ?: 0

        return when (searchQuery) {
            is SearchQuery.Search -> {
                giphyGifsRepository.getGifs(query = searchQuery.query, offset = position)
                    .subscribeOn(Schedulers.io())
                    .map { toLoadResult(it) }
                    .onErrorReturn {
                        LoadResult.Error(it)
                    }
            }
            is SearchQuery.Top -> {
                giphyGifsRepository.getGifsTrending(offset = position)
                    .subscribeOn(Schedulers.io())
                    .map { toLoadResult(it) }
                    .onErrorReturn { LoadResult.Error(it) }
            }
        }
    }

    private fun toLoadResult(giphyGifsResponseDto: GiphyGifsResponseDto): LoadResult<Int, Gif> {
        val totalCount = giphyGifsResponseDto.pagination.totalCount
        val count = giphyGifsResponseDto.pagination.count
        val offset = giphyGifsResponseDto.pagination.offset
        return LoadResult.Page(
            data = mapGifDtoToGif.map(giphyGifsResponseDto),
            prevKey = if (offset <= 0) null else offset - PAGE_SIZE,
            nextKey = if (offset + count >= totalCount) null else offset + PAGE_SIZE
        )
    }

    companion object {
        const val PAGE_SIZE = 30
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted searchQuery: SearchQuery): GifsRxPagingSource
    }
}
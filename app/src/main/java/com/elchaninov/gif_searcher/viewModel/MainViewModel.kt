package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.elchaninov.gif_searcher.Settings
import com.elchaninov.gif_searcher.data.GetGifsRxRepository
import com.elchaninov.gif_searcher.model.Gif
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MainViewModel constructor(
    private val settings: Settings,
    private val getGifsRxRepository: GetGifsRxRepository,
) : ViewModel() {

    var isLinearLayoutManager = settings.isLinearLayoutManager
    private var savedSearchQuery: SearchQuery = SearchQuery.Top
    private var pagingData: Observable<PagingData<Gif>>? = null

    fun getGifs(searchQuery: SearchQuery): Observable<PagingData<Gif>> {
        if (searchQuery is SearchQuery.Empty) pagingData?.let { return it }

        if (savedSearchQuery != searchQuery) {
            savedSearchQuery = searchQuery
            return updateValuePagingData()
        }

        pagingData?.let { return it }
        return updateValuePagingData()
    }

    private fun updateValuePagingData(): Observable<PagingData<Gif>> =
        getGifsRxRepository.getGifs(savedSearchQuery)
            .cachedIn(viewModelScope)
            .also {
                pagingData = it
            }

    fun changeLinearLayoutManager() {
        isLinearLayoutManager = !isLinearLayoutManager
        settings.isLinearLayoutManager = isLinearLayoutManager
    }

    class Factory @Inject constructor(
        private val settings: Settings,
        private val getGifsRxRepository: GetGifsRxRepository,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(settings, getGifsRxRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
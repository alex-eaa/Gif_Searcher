package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.*
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

    private val _pagingDataLiveData: MutableLiveData<Observable<PagingData<Gif>>> =
        MutableLiveData()
    val pagingDataLiveData: LiveData<Observable<PagingData<Gif>>> get() = _pagingDataLiveData

    init {
        _pagingDataLiveData.postValue(updateValuePagingData())
    }

    fun changeSearchQuery(searchQuery: SearchQuery) {
        if (savedSearchQuery != searchQuery) {
            savedSearchQuery = searchQuery
            updateValuePagingData()
        }
    }

    fun changeLinearLayoutManager() {
        isLinearLayoutManager = !isLinearLayoutManager
        settings.isLinearLayoutManager = isLinearLayoutManager
    }

    private fun updateValuePagingData(): Observable<PagingData<Gif>> =
        getGifsRxRepository.getGifs(savedSearchQuery)
            .cachedIn(viewModelScope)
            .also {
                _pagingDataLiveData.postValue(it)
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
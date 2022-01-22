package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.elchaninov.gif_searcher.data.GetGifsRxRepository
import com.elchaninov.gif_searcher.model.Gif
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getGifsRxRepository: GetGifsRxRepository,
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private var savedSearchQuery: SearchQuery = SearchQuery.Top

    private val _pagingDataLiveData: MutableLiveData<PagingData<Gif>> =
        MutableLiveData()

    val pagingDataLiveData: LiveData<PagingData<Gif>> get() = _pagingDataLiveData

    init {
        updateValuePagingData()
    }

    fun changeSearchQuery(searchQuery: SearchQuery) {
        if (savedSearchQuery != searchQuery) {
            savedSearchQuery = searchQuery
            updateValuePagingData()
        }
    }

    private fun updateValuePagingData() {
        disposable.clear()
        disposable.add(getGifsRxRepository.getGifs(savedSearchQuery)
            .cachedIn(viewModelScope)
            .map {
                _pagingDataLiveData.postValue(it)
            }
            .subscribe()
        )
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}
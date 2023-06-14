package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.model.Gif
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GifsViewModel @Inject constructor(
    private val giphyGifsRepository: GiphyGifsRepository,
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
        disposable.add(giphyGifsRepository.getGifs(savedSearchQuery)
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
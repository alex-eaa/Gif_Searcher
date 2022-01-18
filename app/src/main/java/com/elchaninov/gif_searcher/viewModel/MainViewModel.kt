package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.elchaninov.gif_searcher.Settings
import com.elchaninov.gif_searcher.data.GetGifsRxRepository
import com.elchaninov.gif_searcher.data.Gif
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel constructor(
    private val settings: Settings,
    private val getGifsRxRepository: GetGifsRxRepository
) : ViewModel() {

    var isLinearLayoutManager = settings.isLinearLayoutManager
    private var disposables = CompositeDisposable()
    private var stableQuestion: String? = null
    private var pagingData: Observable<PagingData<Gif>>? = null

    fun getGifs(query: String?): Observable<PagingData<Gif>> {
        if (query != stableQuestion) {
            stableQuestion = query
            return updateValuePagingData()
        } else {
            pagingData?.let { return it }
            return updateValuePagingData()
        }
    }

    private fun updateValuePagingData(): Observable<PagingData<Gif>> =
        getGifsRxRepository.getGifs(stableQuestion)
            .cachedIn(viewModelScope)
            .also {
                pagingData = it
            }

    fun changeLinearLayoutManager() {
        isLinearLayoutManager = !isLinearLayoutManager
        settings.isLinearLayoutManager = isLinearLayoutManager
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
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
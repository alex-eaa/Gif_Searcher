package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elchaninov.gif_searcher.Settings
import com.elchaninov.gif_searcher.data.GiphyGifRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel constructor(
    private val giphyGifRepository: GiphyGifRepository,
    private val settings: Settings,
) : ViewModel() {

    var isLinearLayoutManager = settings.isLinearLayoutManager

    private val _appState: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val appState: LiveData<AppState> get() = _appState

    private var disposables = CompositeDisposable()
    private var queryTryAgain: String? = null

    fun fetchGifs(query: String? = null) {
        queryTryAgain = query
        if (query.isNullOrBlank()) searchGifsTrending()
        else searchGifs(query)
    }

    fun tryAgain() {
        fetchGifs(queryTryAgain)
    }

    private fun searchGifs(query: String) {
        disposables.add(
            giphyGifRepository.getGifs(query)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    _appState.postValue(AppState.Loading)
                }
                .subscribe({
                    _appState.postValue(AppState.Success(it))
                }, {
                    _appState.postValue(AppState.Error(it.message.toString()))
                })
        )
    }

    private fun searchGifsTrending() {
        disposables.add(
            giphyGifRepository.getGifsTrending()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    _appState.postValue(AppState.Loading)
                }
                .subscribe({
                    _appState.postValue(AppState.Success(it))
                }, {
                    _appState.postValue(AppState.Error(it.message.toString()))
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        settings.isLinearLayoutManager = isLinearLayoutManager
        disposables.dispose()
    }


    class Factory @Inject constructor(
        private val giphyGifRepository: GiphyGifRepository,
        private val settings: Settings,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(giphyGifRepository, settings) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
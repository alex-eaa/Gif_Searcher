package com.elchaninov.gif_searcher.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.data.GiphyGifRepository
import com.elchaninov.gif_searcher.viewModel.AppState
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var giphyGifRepository: GiphyGifRepository

    var isLinearLayoutManager = true

    private val _appState: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val appState: LiveData<AppState> get() = _appState

    private var queryTryAgain: String? = null

    fun fetchGifs(query: String? = null){
        queryTryAgain = query
        if(query.isNullOrBlank()) searchGifsTrending()
        else searchGifs(query)
    }

    fun tryAgain() {
        fetchGifs(queryTryAgain)
    }

    private fun searchGifs(query: String) {
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
    }

    private fun searchGifsTrending() {
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
    }
}
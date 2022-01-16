package com.elchaninov.gif_searcher.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.data.Gif
import com.elchaninov.gif_searcher.data.GiphyGifRepository
import com.elchaninov.gif_searcher.viewModel.AppState
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var giphyGifRepository: GiphyGifRepository

    private val _gifs: MutableLiveData<List<Gif>> by lazy {
        MutableLiveData<List<Gif>>().also {
            searchGifsTrending()
        }
    }
    val gifs: LiveData<List<Gif>> get() = _gifs

    private val _appState: MutableLiveData<AppState> = MutableLiveData()
    val appState: LiveData<AppState> get() = _appState

    fun searchGifs(query: String) {
        giphyGifRepository.getGifs(query)
            .map {
                Log.d("qqq", "getGifs: ${it}")
                it
            }
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _appState.postValue(AppState.Loading)
            }
            .subscribe({
                _gifs.postValue(it)
            }, {
                _appState.postValue(AppState.Error(it.message.toString()))
            })
    }

    fun searchGifsTrending() {
        giphyGifRepository.getGifsTrending()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                _appState.postValue(AppState.Loading)
            }
            .subscribe({
                _gifs.postValue(it)
            }, {
                _appState.postValue(AppState.Error(it.message.toString()))
            })
    }
}
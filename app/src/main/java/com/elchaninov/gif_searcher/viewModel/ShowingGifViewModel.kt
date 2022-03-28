package com.elchaninov.gif_searcher.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.model.Gif
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

class ShowingGifViewModel @Inject constructor() : ViewModel() {

    private val mDisposable = CompositeDisposable()
    private val viewModelJob = Job()

    private var _fileLiveData: MutableLiveData<CachingState> = MutableLiveData()
    val fileLiveData: LiveData<CachingState> get() = _fileLiveData

    private var _fileLoadProgress: MutableLiveData<Int> = MutableLiveData()
    val fileLoadProgress: LiveData<Int> get() = _fileLoadProgress

    fun fileCaching(gif: Gif, file: File) {
        mDisposable.add(
            Single.just(file)
                .flatMap {
                    if (it.exists() && it.length() == gif.size) Single.just(it)
                    else getRxSavingGif(gif, it)
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _fileLiveData.postValue(CachingState.Success(it))
                }, {
                    _fileLiveData.postValue(CachingState.Failure(it))
                })
        )
    }

    private fun getRxSavingGif(gif: Gif, file: File): Single<File> {
        return Single.create { emitter ->
            try {
                startProgressCalculate(gif, file)

                URL(gif.urlView).openStream().buffered().use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                emitter.onSuccess(file)
            } catch (e: Exception) {
                viewModelJob.cancel()
                emitter.tryOnError(e)
            }
        }
    }

    private fun startProgressCalculate(gif: Gif, file: File) {
        CoroutineScope(Dispatchers.Main + viewModelJob).launch {
            val onePercent = (gif.size ?: 0) / 100
            while (file.length() != gif.size) {
                delay(100)
                _fileLoadProgress.value = (file.length() / onePercent).toInt()
            }
        }
    }

    override fun onCleared() {
        destroyJobs()
        super.onCleared()
    }

    fun destroyJobs() {
        mDisposable.dispose()
        viewModelJob.cancel()
    }
}
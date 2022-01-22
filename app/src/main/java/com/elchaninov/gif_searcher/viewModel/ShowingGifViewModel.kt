package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.model.Gif
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ShowingGifViewModel : ViewModel() {

    private val mDisposable = CompositeDisposable()

    private var _fileLiveData: MutableLiveData<CachingState> = MutableLiveData()
    val fileLiveData: LiveData<CachingState> get() = _fileLiveData

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
                URL(gif.urlView).openStream().buffered().use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                emitter.onSuccess(file)
            } catch (e: Exception) {
                emitter.tryOnError(e)
            }
        }
    }

    override fun onCleared() {
        mDisposable.dispose()
        super.onCleared()
    }

}
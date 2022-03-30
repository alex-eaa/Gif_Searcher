package com.elchaninov.gif_searcher.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elchaninov.gif_searcher.model.Gif
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject

class ShowingGifViewModel @Inject constructor() : ViewModel() {

    private var loadingGifJob = Job()

    private var _fileLiveData: MutableLiveData<CachingState> = MutableLiveData()
    val fileLiveData: LiveData<CachingState> get() = _fileLiveData

    fun fileCaching(gif: Gif, file: File) {
        if (file.exists() && file.length() == gif.size) {
            _fileLiveData.postValue(CachingState.Success(file))
        } else {
            CoroutineScope(Dispatchers.IO + loadingGifJob).launch {
                fetchGif(gif, file)
                    .catch { e ->
                        _fileLiveData.postValue(CachingState.Failure(e))
                    }
                    .collect {
                        _fileLiveData.postValue(it)
                    }
            }
        }
    }

    private suspend fun fetchGif(gif: Gif, file: File) =
        flow {
            Log.d("qqq", "gif url: ${gif.urlView}")
            Log.d("qqq", "gif size: ${gif.size}")

            var inputStream: InputStream? = null

            try {
                val openConnection = URL(gif.urlView).openConnection()

                val contentLength: Int = if (gif.size != null) gif.size.toInt()
                else openConnection.contentLength
                Log.d("qqq", "contentLength: $contentLength")

                inputStream = openConnection.getInputStream()
                val byteArray = ByteArray(contentLength)
                var loadedBytes = 0
                val onePercentRate = contentLength / 100


                var inputByte: Int
                while (true) {
                    inputByte = inputStream.read()
                    if (inputByte == -1) break
                    byteArray[loadedBytes] = inputByte.toByte()
                    loadedBytes++

                    if (loadedBytes % onePercentRate == 0) {
                        Log.d(
                            "qqq",
                            "loaded ${file.name}: $loadedBytes from ${gif.size}.  ${loadedBytes / onePercentRate} %"
                        )
                        emit(CachingState.Progress(loadedBytes / onePercentRate))
                    }
                }

                FileOutputStream(file).use { output ->
                    output.write(byteArray)
                }

                emit(CachingState.Success(file))
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            } finally {
                inputStream?.close()
            }
        }

    fun destroyJobs() {
        loadingGifJob.cancel()
    }

    override fun onCleared() {
        destroyJobs()
        super.onCleared()
    }
}
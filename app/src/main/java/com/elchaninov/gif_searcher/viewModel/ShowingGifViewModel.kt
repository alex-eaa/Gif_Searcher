package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.*
import com.elchaninov.gif_searcher.model.Gif
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject

class ShowingGifViewModel @Inject constructor() : ViewModel() {

    private var _fileLiveData: MutableLiveData<CachingState> =
        MutableLiveData(CachingState.Progress())

    val fileLiveData: LiveData<CachingState> get() = _fileLiveData

    fun fileCaching(gif: Gif, file: File) {
        if (file.exists() && file.length() == gif.size) {
            _fileLiveData.value = CachingState.Success(file)
        } else {
            viewModelScope.launch {
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
            var inputStream: InputStream? = null

            try {
                val openConnection = URL(gif.urlView).openConnection()

                val contentLength: Int = openConnection.contentLength
                val byteArray = ByteArray(contentLength)
                val onePercentRate = contentLength / 100
                var loadedBytes = 0

                inputStream = BufferedInputStream(openConnection.getInputStream())

                var inputByte: Int
                while (true) {
                    inputByte = inputStream.read()
                    if (inputByte == -1) break
                    byteArray[loadedBytes] = inputByte.toByte()
                    loadedBytes++

                    if (loadedBytes % onePercentRate == 0) {
                        emit(CachingState.Progress(loadedBytes / onePercentRate))
                    }
                }

                if (loadedBytes > 0) saveToFile(byteArray, file)
                emit(CachingState.Success(file))
            } catch (e: Exception) {
                throw e
            } finally {
                inputStream?.close()
            }
        }.flowOn(Dispatchers.IO)

    private fun saveToFile(byteArray: ByteArray, file: File) {
        try {
            FileOutputStream(file).buffered().use { output ->
                output.write(byteArray)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
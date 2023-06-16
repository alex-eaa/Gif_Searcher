package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elchaninov.gif_searcher.data.FavoritesRepository
import com.elchaninov.gif_searcher.model.Gif
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FullGifViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private var _fileLiveData: MutableLiveData<LoadingState<File>> =
        MutableLiveData(LoadingState.Progress())
    val fileLiveData: LiveData<LoadingState<File>> get() = _fileLiveData

    private val initFlow = MutableStateFlow<Gif?>(null)
    val isFavoriteGif = initFlow
        .filterNotNull()
        .flatMapLatest {
            favoritesRepository.isFavoriteGifFlow(it.id)
        }

    fun fileCaching(gif: Gif, file: File) {
        if (file.exists() && file.length() == gif.sizeOriginal) {
            _fileLiveData.value = LoadingState.Success(file)
        } else {
            viewModelScope.launch {
                fetchGif(gif, file)
                    .catch { e ->
                        _fileLiveData.postValue(LoadingState.Failure(e))
                    }
                    .collect {
                        _fileLiveData.postValue(it)
                    }
            }
        }

        initFlow.tryEmit(gif)
    }

    fun toggleGifFavorite(gif: Gif) {
        viewModelScope.launch {
            favoritesRepository.toggleGifFavorite(gif)
        }
    }

    private suspend fun fetchGif(gif: Gif, file: File) =
        flow {
            var inputStream: InputStream? = null

            try {
                val openConnection = URL(gif.urlOriginal).openConnection()

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
                        emit(LoadingState.Progress(loadedBytes / onePercentRate))
                    }
                }

                if (loadedBytes > 0) saveToFile(byteArray, file)
                emit(LoadingState.Success(file))
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
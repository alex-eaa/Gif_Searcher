package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.model.Category
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel @Inject constructor(
    private val giphyGifsRepository: GiphyGifsRepository,
) : ViewModel() {
    private val _dataLiveData: MutableLiveData<LoadingState<List<Category>>> = MutableLiveData()
    val dataLiveData: LiveData<LoadingState<List<Category>>> get() = _dataLiveData

    init {
        _dataLiveData.postValue(LoadingState.Progress())
        updateData()
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                giphyGifsRepository.getCategories().let {
                    val list = mutableListOf(Category.createTrendingCategory())
                    list.addAll(it)
                    _dataLiveData.postValue(LoadingState.Success(list))
                }
            } catch (e: Exception) {
                _dataLiveData.postValue(LoadingState.Failure(e))
            }
        }
    }
}
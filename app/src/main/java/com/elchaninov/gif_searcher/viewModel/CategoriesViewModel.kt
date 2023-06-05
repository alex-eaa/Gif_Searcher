package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.model.Category
import com.elchaninov.gif_searcher.model.Category.Companion.isSubcategory
import com.elchaninov.gif_searcher.model.Subcategory.Companion.asCategory
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel @Inject constructor(
    private val giphyGifsRepository: GiphyGifsRepository,
) : ViewModel() {
    private val _dataLiveData: MutableLiveData<LoadingState<List<Category>>> = MutableLiveData()
    val dataLiveData: LiveData<LoadingState<List<Category>>> get() = _dataLiveData

    var isShowCollapseItemMenuLiveData: Boolean = false

    init {
        _dataLiveData.postValue(LoadingState.Progress())
        updateData()
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                giphyGifsRepository.getCategories().let {
                    val list = arrayListOf(Category.createTrendingCategory())
                    list.addAll(it)
                    _dataLiveData.postValue(LoadingState.Success(list))
                }
            } catch (e: Exception) {
                _dataLiveData.postValue(LoadingState.Failure(e))
            }
        }
    }

    fun onClickCategory(category: Category) {
        if (category.isExpanded) {
            collapseCategory(category)
            isShowCollapseItemMenuLiveData = hasExpandedSubcategories()
        } else {
            isShowCollapseItemMenuLiveData = true
            expandCategory(category)
        }
    }

    private fun expandCategory(category: Category) {
        (_dataLiveData.value as? LoadingState.Success)?.file?.toMutableList()?.let { categoryList ->
            categoryList.indexOf(category).let {
                val oldCategory = categoryList[it]
                categoryList[it] = oldCategory.copy(isExpanded = !oldCategory.isExpanded)

                val subcategoryList = mutableListOf<Category>()
                categoryList[it].subcategories.map { subcategory ->
                    subcategoryList.add(subcategory.asCategory())
                }

                categoryList.addAll(it + 1, subcategoryList)
            }
            _dataLiveData.postValue(LoadingState.Success(categoryList))
        }
    }

    private fun collapseCategory(category: Category) {
        (_dataLiveData.value as? LoadingState.Success)?.file?.toMutableList()?.let { categoryList ->
            categoryList.indexOf(category).let {
                val oldCategory = categoryList[it]
                categoryList[it] = oldCategory.copy(isExpanded = !oldCategory.isExpanded)

                oldCategory.subcategories.forEach { subcategory ->
                    val index = categoryList.indexOfFirst { category ->
                        category.name == subcategory.nameEncoded
                    }
                    categoryList.removeAt(index)
                }
                _dataLiveData.postValue(LoadingState.Success(categoryList))
            }
        }
    }

    fun collapseAll() {
        (_dataLiveData.value as? LoadingState.Success)?.file?.toMutableList()?.let { categoryList ->
            categoryList.forEachIndexed { index, category ->
                categoryList[index] = category.copy(isExpanded = false)
            }

            categoryList.removeAll {
                it.name != null && it.isSubcategory()
            }

            isShowCollapseItemMenuLiveData = false
            _dataLiveData.postValue(LoadingState.Success(categoryList))
        }
    }

    private fun hasExpandedSubcategories(): Boolean {
        return (_dataLiveData.value as? LoadingState.Success)?.file?.let { categoryList ->
            categoryList.find { it.isExpanded }
        } == null
    }
}
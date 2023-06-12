package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.model.SubcategoryModel.Companion.asSubcategory
import com.elchaninov.gif_searcher.model.TypedCategory
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CategoriesViewModel @Inject constructor(
    private val giphyGifsRepository: GiphyGifsRepository,
) : ViewModel() {
    private val _dataLiveData: MutableLiveData<LoadingState<List<TypedCategory>>> =
        MutableLiveData()
    val dataLiveData: LiveData<LoadingState<List<TypedCategory>>> get() = _dataLiveData

    var isShowCollapseItemMenuLiveData: Boolean = false

    init {
        _dataLiveData.postValue(LoadingState.Progress())


        viewModelScope.launch(Dispatchers.IO) {
            updateData()

            giphyGifsRepository.isFavoritesNotEmpty()
                .collect { isFavoritesNotEmpty ->
                    (_dataLiveData.value as? LoadingState.Success)?.file?.toMutableList()
                        ?.let { typedCategoryList ->
                            if (isFavoritesNotEmpty && (typedCategoryList[0] !is TypedCategory.Custom.Favorite || typedCategoryList.isEmpty()))
                                typedCategoryList.add(0, TypedCategory.Custom.Favorite())
                            else if (!isFavoritesNotEmpty && typedCategoryList[0] is TypedCategory.Custom.Favorite)
                                typedCategoryList.removeAt(0)

                            _dataLiveData.postValue(LoadingState.Success(typedCategoryList))
                        }
                }
        }
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                giphyGifsRepository.getCategories().let {
                    val list: MutableList<TypedCategory> = mutableListOf()

                    if (giphyGifsRepository.isFavoritesNotEmpty().firstOrNull() == true) {
                        list.add(TypedCategory.Custom.Favorite())
                    }

                    list.add(TypedCategory.Custom.Trending())
                    list.addAll(it)
                    _dataLiveData.postValue(LoadingState.Success(list))
                }
            } catch (e: Exception) {
                _dataLiveData.postValue(LoadingState.Failure(e))
            }
        }
    }

    fun onClickCategory(category: TypedCategory.Category) {
        if (category.isExpanded) {
            collapseCategory(category)
            isShowCollapseItemMenuLiveData = hasExpandedSubcategories()
        } else {
            expandCategory(category)
            isShowCollapseItemMenuLiveData = true
        }
    }

    fun collapseAll() {
        (_dataLiveData.value as? LoadingState.Success)?.file?.toMutableList()
            ?.let { typedCategoryList ->
                typedCategoryList.forEachIndexed { index, typedCategory ->
                    (typedCategory as? TypedCategory.Category)?.let {
                        typedCategoryList[index] = it.copy(isExpanded = false)
                    }
                }

                typedCategoryList.removeAll {
                    it is TypedCategory.Subcategory
                }

                isShowCollapseItemMenuLiveData = false
                _dataLiveData.value = LoadingState.Success(typedCategoryList)
            }
    }

    private fun expandCategory(category: TypedCategory.Category) {
        (_dataLiveData.value as? LoadingState.Success)?.file?.toMutableList()
            ?.let { typedCategoryList ->
                typedCategoryList.indexOf(category).let { index ->
                    typedCategoryList[index] = category.copy(isExpanded = true)

                    val subcategoryList = mutableListOf<TypedCategory.Subcategory>()
                    category.subcategories.map { subcategoryModel ->
                        subcategoryList.add(subcategoryModel.asSubcategory())
                    }
                    typedCategoryList.addAll(index + 1, subcategoryList)
                }
                _dataLiveData.value = LoadingState.Success(typedCategoryList)
            }
    }

    private fun collapseCategory(category: TypedCategory.Category) {
        (_dataLiveData.value as? LoadingState.Success)?.file?.toMutableList()
            ?.let { typedCategoryList ->
                typedCategoryList.indexOf(category).let { index ->
                    typedCategoryList[index] = category.copy(isExpanded = false)

                    category.subcategories.forEach { subcategoryModel ->
                        val i = typedCategoryList.indexOfFirst {
                            it is TypedCategory.Subcategory && it.name == subcategoryModel.nameEncoded
                        }
                        if (i >= 0) typedCategoryList.removeAt(index)
                    }
                    _dataLiveData.value = LoadingState.Success(typedCategoryList)
                }
            }
    }

    private fun hasExpandedSubcategories(): Boolean {
        (_dataLiveData.value as? LoadingState.Success)?.file?.let { categoryList ->
            categoryList.forEach {
                if (it is TypedCategory.Category && it.isExpanded) return true
            }
        }
        return false
    }
}
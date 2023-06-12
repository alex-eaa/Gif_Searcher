package com.elchaninov.gif_searcher.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elchaninov.gif_searcher.data.GiphyGifsRepository
import com.elchaninov.gif_searcher.model.SubcategoryModel.Companion.asSubcategory
import com.elchaninov.gif_searcher.model.TypedCategory
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CategoriesViewModel @Inject constructor(
    private val giphyGifsRepository: GiphyGifsRepository,
) : ViewModel() {

    private val _isShowCollapseItemMenuFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isShowCollapseItemMenuFlow: StateFlow<Boolean> = _isShowCollapseItemMenuFlow

    private val loadingStateFlow: MutableStateFlow<LoadingState<List<TypedCategory>>> =
        MutableStateFlow(LoadingState.Progress())

    val combinedLoadingStateFlow: Flow<LoadingState<List<TypedCategory>>> = combine(
        loadingStateFlow,
        giphyGifsRepository.isFavoritesNotEmpty()
    ) { typedCategoryList, isFavoritesNotEmpty ->
        if (typedCategoryList is LoadingState.Success) {
            val list: MutableList<TypedCategory> = mutableListOf()
            if (isFavoritesNotEmpty) {
                list.add(TypedCategory.Custom.Favorite(isExpanded = false))
                list.add(TypedCategory.Custom.Trending(isExpanded = false))
            } else {
                list.add(TypedCategory.Custom.Trending(isExpanded = true))
            }
            list.addAll(typedCategoryList.file)
            LoadingState.Success(list)
        } else {
            typedCategoryList
        }
    }

    init {
        updateData()
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingStateFlow.value = LoadingState.Progress()
            try {
                loadingStateFlow.value = LoadingState.Success(giphyGifsRepository.getCategories())
            } catch (e: Exception) {
                loadingStateFlow.value = LoadingState.Failure(e)
            }
        }
    }

    fun onClickCategory(category: TypedCategory.Category) {
        if (category.isExpanded) {
            collapseCategory(category)
            _isShowCollapseItemMenuFlow.value = hasExpandedSubcategories()
        } else {
            expandCategory(category)
            _isShowCollapseItemMenuFlow.value = true
        }
    }

    fun collapseAll() {
        (loadingStateFlow.value as? LoadingState.Success)?.file?.toMutableList()
            ?.let { typedCategoryList ->
                typedCategoryList.forEachIndexed { index, typedCategory ->
                    (typedCategory as? TypedCategory.Category)?.let {
                        typedCategoryList[index] = it.copy(isExpanded = false)
                    }
                }

                typedCategoryList.removeAll {
                    it is TypedCategory.Subcategory
                }

                _isShowCollapseItemMenuFlow.value = false
                loadingStateFlow.value = LoadingState.Success(typedCategoryList)
            }
    }

    private fun expandCategory(category: TypedCategory.Category) {
        (loadingStateFlow.value as? LoadingState.Success)?.file?.toMutableList()
            ?.let { typedCategoryList ->
                typedCategoryList.indexOf(category).let { index ->
                    typedCategoryList[index] = category.copy(isExpanded = true)

                    val subcategoryList = mutableListOf<TypedCategory.Subcategory>()
                    category.subcategories.map { subcategoryModel ->
                        subcategoryList.add(subcategoryModel.asSubcategory())
                    }
                    typedCategoryList.addAll(index + 1, subcategoryList)
                }
                loadingStateFlow.value = LoadingState.Success(typedCategoryList)
            }
    }

    private fun collapseCategory(category: TypedCategory.Category) {
        (loadingStateFlow.value as? LoadingState.Success)?.file?.toMutableList()
            ?.let { typedCategoryList ->
                typedCategoryList.indexOf(category).let { index ->
                    typedCategoryList[index] = category.copy(isExpanded = false)

                    category.subcategories.forEach { subcategoryModel ->
                        val i = typedCategoryList.indexOfFirst {
                            it is TypedCategory.Subcategory && it.name == subcategoryModel.nameEncoded
                        }
                        if (i != -1) typedCategoryList.removeAt(i)
                    }
                    loadingStateFlow.value = LoadingState.Success(typedCategoryList)
                }
            }
    }

    private fun hasExpandedSubcategories(): Boolean {
        (loadingStateFlow.value as? LoadingState.Success)?.file?.let { categoryList ->
            categoryList.forEach {
                if (it is TypedCategory.Category && it.isExpanded) return true
            }
        }
        return false
    }
}
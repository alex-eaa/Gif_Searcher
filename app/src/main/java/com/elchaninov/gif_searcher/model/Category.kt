package com.elchaninov.gif_searcher.model

import androidx.annotation.StringRes
import com.elchaninov.gif_searcher.R

sealed class TypedCategory {
    sealed class Custom(@StringRes open val name: Int) : TypedCategory() {
        data class Favorite(@StringRes override val name: Int = R.string.favorites) : Custom(name)
        data class Trending(@StringRes override val name: Int = R.string.top) : Custom(name)
    }

    data class Subcategory(val name: String) : TypedCategory()

    data class Category(
        val name: String,
        val gif: Gif,
        val subcategories: List<SubcategoryModel>,
        val isExpanded: Boolean = false,
    ) : TypedCategory()
}


data class SubcategoryModel(
    val nameEncoded: String,
) {
    companion object {
        fun SubcategoryModel.asSubcategory() = TypedCategory.Subcategory(nameEncoded)
    }
}
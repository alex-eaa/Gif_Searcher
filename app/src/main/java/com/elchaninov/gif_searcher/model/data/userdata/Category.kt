package com.elchaninov.gif_searcher.model.data.userdata

import androidx.annotation.StringRes
import com.elchaninov.gif_searcher.R

sealed class TypedCategory(open val name: String) {
    sealed class Custom(
        override val name: String,
        @StringRes open val nameId: Int,
        open val isExpanded: Boolean
    ) : TypedCategory(name) {
        data class Favorite(
            @StringRes override val nameId: Int = R.string.favorites,
            override val isExpanded: Boolean,
        ) : Custom(nameId.toString(), nameId, isExpanded)

        data class Trending(
            @StringRes override val nameId: Int = R.string.top,
            override val isExpanded: Boolean,
        ) : Custom(nameId.toString(), nameId, isExpanded)
    }

    data class Subcategory(override val name: String) : TypedCategory(name)

    data class Category(
        override val name: String,
        val gif: Gif,
        val subcategories: List<SubcategoryModel>,
        val isExpanded: Boolean = false,
    ) : TypedCategory(name)
}


data class SubcategoryModel(
    val nameEncoded: String,
) {
    companion object {
        fun SubcategoryModel.asSubcategory() = TypedCategory.Subcategory(nameEncoded)
    }
}
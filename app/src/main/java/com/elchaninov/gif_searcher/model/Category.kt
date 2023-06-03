package com.elchaninov.gif_searcher.model

data class Category(
    val name: String?,
    val gif: Gif?,
    val subcategories: List<Subcategory>,
    val isExpanded: Boolean = false,
) {
    companion object {
        fun createTrendingCategory() = Category(null, null, emptyList())
    }
}

data class Subcategory(
    val nameEncoded: String,
)

fun Subcategory.asCategory(): Category {
    return Category(nameEncoded, null, emptyList())
}
package com.elchaninov.gif_searcher.model

data class Category(
    val name: String?,
    val gif: Gif?,
    val subcategories: List<Subcategory>,
) {
    companion object {
        fun empty() = Category(null, null, emptyList())
    }
}

data class Subcategory(
    val nameEncoded: String,
)
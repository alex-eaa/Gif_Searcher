package com.elchaninov.gif_searcher.data.api

import com.elchaninov.gif_searcher.model.Gif

data class CategoryResponseDto(
    val data: List<CategoryDto>,
)

data class  CategoryDto(
    val name: String?,
    val gif: Gif?,
)


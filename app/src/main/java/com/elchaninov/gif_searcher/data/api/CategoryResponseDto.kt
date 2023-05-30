package com.elchaninov.gif_searcher.data.api

data class CategoryResponseDto(
    val data: List<CategoryDto>,
)

data class CategoryDto(
    val name: String,
    val gif: GifDto,
)


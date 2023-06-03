package com.elchaninov.gif_searcher.data.api

import com.google.gson.annotations.SerializedName

data class CategoryResponseDto(
    val data: List<CategoryDto>,
)

data class CategoryDto(
    val name: String,
    val gif: GifDto,
    val subcategories: List<SubcategoryDto>,
)

data class SubcategoryDto(
    val name: String,
    @SerializedName("name_encoded") val nameEncoded: String,
)
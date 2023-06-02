package com.elchaninov.gif_searcher.data.mappers

import com.elchaninov.gif_searcher.data.api.CategoryDto
import com.elchaninov.gif_searcher.model.Category
import javax.inject.Inject

class MapCategoryDtoToCategory @Inject constructor(
    private val mapGifDtoToGif: MapGifDtoToGif
) {

    fun map(categoryDto: CategoryDto): Category {
        return Category(
            name = categoryDto.name,
            gif = mapGifDtoToGif.map(categoryDto.gif),
        )
    }
}
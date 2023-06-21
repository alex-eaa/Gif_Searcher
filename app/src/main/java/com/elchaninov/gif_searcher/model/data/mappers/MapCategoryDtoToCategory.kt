package com.elchaninov.gif_searcher.model.data.mappers

import com.elchaninov.gif_searcher.model.data.dto.CategoryDto
import com.elchaninov.gif_searcher.model.data.dto.SubcategoryDto
import com.elchaninov.gif_searcher.model.data.userdata.SubcategoryModel
import com.elchaninov.gif_searcher.model.data.userdata.TypedCategory
import javax.inject.Inject

class MapCategoryDtoToCategory @Inject constructor(
    private val mapGifDtoToGif: MapGifDtoToGif
) {

    fun map(categoryDto: CategoryDto): TypedCategory.Category {
        return TypedCategory.Category(
            name = categoryDto.name,
            gif = mapGifDtoToGif.map(categoryDto.gif),
            subcategories = categoryDto.subcategories.map {
                map(it)
            }
        )
    }

    private fun map(subcategoryDto: SubcategoryDto): SubcategoryModel {
        return SubcategoryModel(
            nameEncoded = subcategoryDto.nameEncoded
        )
    }
}
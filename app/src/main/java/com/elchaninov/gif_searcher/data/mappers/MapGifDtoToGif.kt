package com.elchaninov.gif_searcher.data.mappers

import com.elchaninov.gif_searcher.data.Gif
import com.elchaninov.gif_searcher.data.retrofit.GifDto
import javax.inject.Inject

class MapGifDtoToGif @Inject constructor() {

    fun map(gifDto: GifDto): List<Gif> {
        return gifDto.data.map {
            Gif(
                type = it.type,
                title = it.title,
                urlPreview = it.images.previewGif.url,
                urlView = it.images.original.url
            )
        }
    }
}
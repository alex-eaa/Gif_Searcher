package com.elchaninov.gif_searcher.model.data.mappers

import com.elchaninov.gif_searcher.model.data.dto.GifDto
import com.elchaninov.gif_searcher.model.data.dto.GiphyGifsResponseDto
import com.elchaninov.gif_searcher.model.data.userdata.Gif
import javax.inject.Inject

class MapGifDtoToGif @Inject constructor() {

    fun map(giphyGifsResponseDto: GiphyGifsResponseDto): List<Gif> {
        return giphyGifsResponseDto.data.map {
            map(it)
        }
    }

    fun map(gifDto: GifDto): Gif {
        return Gif(
            id = gifDto.id,
            type = gifDto.type,
            title = gifDto.title,
            urlPreview = gifDto.images.previewGif?.url,
            heightPreview = gifDto.images.previewGif?.height,
            widthPreview = gifDto.images.previewGif?.width,
            urlOriginal = gifDto.images.original.url,
            sizeOriginal = gifDto.images.original.size
        )
    }
}
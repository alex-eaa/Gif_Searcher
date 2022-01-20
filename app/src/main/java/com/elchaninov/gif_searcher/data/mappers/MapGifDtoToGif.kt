package com.elchaninov.gif_searcher.data.mappers

import com.elchaninov.gif_searcher.data.api.GiphyGifsResponse
import com.elchaninov.gif_searcher.model.Gif
import javax.inject.Inject

class MapGifDtoToGif @Inject constructor() {

    fun map(giphyGifsResponse: GiphyGifsResponse): List<Gif> {
        return giphyGifsResponse.data.map {
            Gif(
                id = it.id,
                type = it.type,
                title = it.title,
                urlPreview = it.images.previewGif.url,
                urlView = it.images.original.url
            )
        }
    }
}
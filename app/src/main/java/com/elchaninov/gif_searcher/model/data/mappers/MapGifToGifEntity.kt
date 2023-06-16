package com.elchaninov.gif_searcher.model.data.mappers

import com.elchaninov.gif_searcher.model.data.userdata.Gif
import com.elchaninov.gif_searcher.model.datasource.room.entity.GifEntity
import javax.inject.Inject

class MapGifToGifEntity @Inject constructor() {

    fun map(gif: Gif): GifEntity {
        return GifEntity(
            id = gif.id,
            type = gif.type,
            title = gif.title,
            urlPreview = gif.urlPreview,
            heightPreview = gif.heightPreview,
            widthPreview = gif.widthPreview,
            urlOriginal = gif.urlOriginal,
            sizeOriginal = gif.sizeOriginal,
            dateCreated = System.currentTimeMillis(),
        )
    }

    fun map(gifEntity: GifEntity): Gif {
        return Gif(
            id = gifEntity.id,
            type = gifEntity.type,
            title = gifEntity.title,
            urlPreview = gifEntity.urlPreview,
            heightPreview = gifEntity.heightPreview,
            widthPreview = gifEntity.widthPreview,
            urlOriginal = gifEntity.urlOriginal,
            sizeOriginal = gifEntity.sizeOriginal,
        )
    }
}
package com.elchaninov.gif_searcher.data.api

import com.google.gson.annotations.SerializedName

data class GiphyGifsResponseDto(
    val data: List<DataDto>,
    val pagination: PaginationDto,
)

data class DataDto(
    val id: String?,
    val type: String?,
    val title: String?,
    val images: ImagesDto,
)

data class ImagesDto(
    val original: OriginalDto,
    @SerializedName("preview_gif")
    val previewGif: PreviewGifDto,
)

data class OriginalDto(
    val url: String?,
    val size: Long,
)

data class PreviewGifDto(
    val url: String?,
)

data class PaginationDto(
    @SerializedName("total_count")
    val totalCount: Int,
    val count: Int,
    val offset: Int,
)


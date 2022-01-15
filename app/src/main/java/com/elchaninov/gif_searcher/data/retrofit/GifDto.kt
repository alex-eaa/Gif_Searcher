package com.elchaninov.gif_searcher.data.retrofit

import com.google.gson.annotations.SerializedName

data class GifDto(
    @SerializedName("data")
    val data: List<DataDto>
) {
    data class DataDto(
        @SerializedName("id")
        val id: String?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("images")
        val images: ImagesDto
    )

    data class ImagesDto(
        @SerializedName("original")
        val original: OriginalDto,
        @SerializedName("preview_gif")
        val previewGif: PreviewGifDto
    )

    data class OriginalDto(
        @SerializedName("url")
        val url: String?
    )

    data class PreviewGifDto(
        @SerializedName("url")
        val url: String?
    )
}






package com.elchaninov.gif_searcher.data.retrofit

import com.google.gson.annotations.SerializedName

data class GifDto(
    @SerializedName("data")
    val data: List<DataDto>,
    @SerializedName("pagination")
    val pagination: PaginationDto,
    @SerializedName("meta")
    val meta: MetaDto,
) {

    data class DataDto(
        @SerializedName("id")
        val id: String?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("images")
        val images: ImagesDto,
    ) {
        data class ImagesDto(
            @SerializedName("original")
            val original: OriginalDto,
            @SerializedName("preview_gif")
            val previewGif: PreviewGifDto,
        )

        data class OriginalDto(
            @SerializedName("url")
            val url: String?,
        )

        data class PreviewGifDto(
            @SerializedName("url")
            val url: String?,
        )
    }

    data class PaginationDto(
        @SerializedName("total_count")
        val totalCount: Int,
        @SerializedName("count")
        val count: Int,
        @SerializedName("offset")
        val offset: Int,
    )

    data class MetaDto(
        @SerializedName("status")
        val status: Int?,
        @SerializedName("msg")
        val msg: String?,
        @SerializedName("response_id")
        val responseId: String?,
    )
}




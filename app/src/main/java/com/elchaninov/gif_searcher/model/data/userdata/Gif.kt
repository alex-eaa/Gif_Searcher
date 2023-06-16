package com.elchaninov.gif_searcher.model.data.userdata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gif(
    val id: String,
    val type: String?,
    val title: String?,
    val urlPreview: String?,
    val heightPreview: Int?,
    val widthPreview: Int?,
    val urlOriginal: String,
    val sizeOriginal: Long,
) : Parcelable

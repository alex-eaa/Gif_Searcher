package com.elchaninov.gif_searcher.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gif(
    val id: String?,
    val type: String?,
    val title: String?,
    val urlPreview: String?,
    val urlView: String?,
) : Parcelable

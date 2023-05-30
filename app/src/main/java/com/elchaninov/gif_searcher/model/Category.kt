package com.elchaninov.gif_searcher.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val name: String?,
    val gif: Gif?,
) : Parcelable {
    companion object {
        fun empty() = Category(null, null)
    }
}
package com.elchaninov.gif_searcher.ui.favorites

import androidx.recyclerview.widget.DiffUtil
import com.elchaninov.gif_searcher.model.Gif

class GifsDiffUtilCallback(
    private val oldList: List<Gif>,
    private val newList: List<Gif>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGif: Gif = oldList[oldItemPosition]
        val newGif: Gif = newList[newItemPosition]
        return oldGif.id == newGif.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGif: Gif = oldList[oldItemPosition]
        val newGif: Gif = newList[newItemPosition]
        return (oldGif.id == newGif.id
                && oldGif.title == newGif.title
                && oldGif.urlPreview == newGif.urlPreview
                && oldGif.urlOriginal == newGif.urlOriginal
                && oldGif.sizeOriginal == newGif.sizeOriginal
                )
    }
}
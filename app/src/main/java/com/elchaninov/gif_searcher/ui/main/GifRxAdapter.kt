package com.elchaninov.gif_searcher.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.elchaninov.gif_searcher.data.Gif

class GifsRxAdapter(
    var itemLayoutForInflate: Int,
    private val onItemClickListener: OnItemClickListener
) : PagingDataAdapter<Gif, GifViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayoutForInflate, parent, false)

        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onItemClickListener)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Gif>() {
            override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean {
                return oldItem == newItem
            }
        }
    }
}
package com.elchaninov.gif_searcher.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elchaninov.gif_searcher.data.Gif


class GifAdapter(
    var itemLayoutForInflate: Int,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<GifViewHolder>() {

    var data: List<Gif> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayoutForInflate, parent, false)

        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.bind(data[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnItemClickListener {
        fun onItemClick(gif: Gif)
    }
}
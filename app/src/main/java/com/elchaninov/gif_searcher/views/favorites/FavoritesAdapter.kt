package com.elchaninov.gif_searcher.views.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elchaninov.gif_searcher.model.data.userdata.Gif
import com.elchaninov.gif_searcher.views.gifs.GifViewHolder


class FavoritesAdapter(
    private val itemLayoutForInflate: Int,
    private val onItemClickListener: (Gif) -> Unit,
) : RecyclerView.Adapter<GifViewHolder>() {
    private val dataList: MutableList<Gif> = mutableListOf()

    var lastPosition = 0
    var direction = false

    fun setItems(categories: List<Gif>) {
        dataList.clear()
        dataList.addAll(categories)
    }

    fun getItems(): List<Gif> = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayoutForInflate, parent, false)
        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        direction = lastPosition > position
        lastPosition = position

        holder.bind(dataList[position], onClick = onItemClickListener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
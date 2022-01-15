package com.elchaninov.gif_searcher.ui.main

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.data.Gif


class GifViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(gif: Gif) {
        val imageView: ImageView? = itemView.findViewById(R.id.image_view)
        val imageTitle: TextView? = itemView.findViewById(R.id.image_title)

        imageTitle?.text = gif.title

        imageView?.let {
            Glide
                .with(it)
                .asGif()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .load(gif.urlPreview)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)
        }
    }
}
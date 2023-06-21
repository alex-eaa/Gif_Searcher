package com.elchaninov.gif_searcher.views.gifs

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.data.userdata.Gif


class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(gif: Gif, onClick: (Gif) -> Unit) {
        val imageView: ImageView? = itemView.findViewById(R.id.image_view)
        val imageTitle: TextView? = itemView.findViewById(R.id.image_title)

        imageTitle?.text = gif.title

        imageView?.let {
            Glide
                .with(it)
                .asGif()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .load(gif.urlPreview)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)
        }

        itemView.rootView.setOnClickListener {
            onClick(gif)
        }
    }
}
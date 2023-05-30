package com.elchaninov.gif_searcher.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.Category
import com.elchaninov.gif_searcher.model.Gif


class CategoryViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(category: Category, onItemClickListener: CategoriesAdapter.OnItemClickListener) {
        val imageView: ImageView? = itemView.findViewById(R.id.category_image_view)
        val imageTitle: TextView? = itemView.findViewById(R.id.category_title)

        imageTitle?.text = category.name

        imageView?.let {
            Glide
                .with(it)
                .asGif()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .load(category.gif.urlPreview)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)
        }

        itemView.rootView.setOnClickListener {
            onItemClickListener.onItemClick(category)
        }
    }
}
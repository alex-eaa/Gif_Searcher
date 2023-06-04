package com.elchaninov.gif_searcher.ui.categories

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.Category


class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: Category, onClick: (Category) -> Unit) {
        category.name?.let { name ->
            val imageTitle: TextView? = itemView.findViewById(R.id.category_title)
            imageTitle?.text = name
        }

        val imageView: ImageView? = itemView.findViewById(R.id.category_image_view)
        imageView?.let {
            if (category.gif != null) {
                imageView.visibility = View.VISIBLE
                Glide
                    .with(imageView)
                    .asGif()
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .load(category.gif.urlPreview)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageView)
            } else {
                imageView.visibility = View.GONE
            }
        }

        itemView.rootView.setOnClickListener {
            onClick(category)
        }
    }
}
package com.elchaninov.gif_searcher.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elchaninov.gif_searcher.model.Category


class TrendingCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: Category, onClick: (Category) -> Unit) {
        itemView.rootView.setOnClickListener {
            onClick(category)
        }
    }
}
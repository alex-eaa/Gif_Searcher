package com.elchaninov.gif_searcher.ui.categories

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.TypedCategory


class CustomCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: TypedCategory.Custom, onClick: (TypedCategory) -> Unit) {
        val categoryTitle: TextView? = itemView.findViewById(R.id.custom_category_title)
        categoryTitle?.let {
            it.text = itemView.resources.getString(category.nameId)
        }
        itemView.rootView.setOnClickListener {
            onClick(category)
        }
    }
}
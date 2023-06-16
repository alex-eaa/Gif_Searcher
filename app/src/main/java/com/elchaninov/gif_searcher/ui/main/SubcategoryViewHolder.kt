package com.elchaninov.gif_searcher.ui.main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.TypedCategory


class SubcategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: TypedCategory.Subcategory, onClick: (TypedCategory) -> Unit) {
        val imageTitle: TextView? = itemView.findViewById(R.id.subcategory_title)
        imageTitle?.text = category.name

        itemView.rootView.setOnClickListener {
            onClick(category)
        }
    }
}
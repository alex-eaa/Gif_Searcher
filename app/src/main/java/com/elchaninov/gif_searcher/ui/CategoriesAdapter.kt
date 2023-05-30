package com.elchaninov.gif_searcher.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elchaninov.gif_searcher.model.Category

class CategoriesAdapter(
    private val itemLayoutForInflate: Int,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CategoryViewHolder>() {
    private var categoryList: MutableList<Category> = mutableListOf()

    fun setItems(categories: Collection<Category>) {
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayoutForInflate, parent, false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }


    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }
}
package com.elchaninov.gif_searcher.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.Category


class CategoriesAdapter(
    private val onItemClickListener: (Category) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val categoryList: MutableList<Category> = mutableListOf()

    fun setItems(categories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(categories)
    }

    fun getItems(): List<Category> = categoryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TRENDING_CATEGORIES_VIEW_TYPE -> {
                val itemView = inflater.inflate(R.layout.trending_category_item, parent, false)
                val layoutParams =
                    StaggeredGridLayoutManager.LayoutParams(itemView.layoutParams).apply {
                        isFullSpan = true
                    }
                itemView.layoutParams = layoutParams
                TrendingCategoryViewHolder(itemView)
            }
            COLLAPSE_CATEGORIES_VIEW_TYPE -> {
                val itemView = inflater.inflate(R.layout.collapsed_category_item, parent, false)
                CategoryViewHolder(itemView)
            }
            EXPANDED_CATEGORIES_VIEW_TYPE -> {
                val itemView = inflater.inflate(R.layout.expanded_category_item, parent, false)
                val layoutParams =
                    StaggeredGridLayoutManager.LayoutParams(itemView.layoutParams).apply {
                        isFullSpan = true
                    }
                itemView.layoutParams = layoutParams
                CategoryViewHolder(itemView)
            }
            else -> {
                val itemView = inflater.inflate(R.layout.subcategory_item, parent, false)
                CategoryViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> {
                holder.bind(
                    category = categoryList[position],
                    onClick = onItemClickListener,
                )
            }
            is TrendingCategoryViewHolder -> {
                holder.bind(categoryList[position], onItemClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TRENDING_CATEGORIES_VIEW_TYPE
            categoryList[position].subcategories.isEmpty() -> SUBCATEGORIES_VIEW_TYPE
            categoryList[position].isExpanded -> EXPANDED_CATEGORIES_VIEW_TYPE
            else -> COLLAPSE_CATEGORIES_VIEW_TYPE
        }
    }

    companion object {
        const val TRENDING_CATEGORIES_VIEW_TYPE = 0
        const val COLLAPSE_CATEGORIES_VIEW_TYPE = 1
        const val EXPANDED_CATEGORIES_VIEW_TYPE = 2
        const val SUBCATEGORIES_VIEW_TYPE = 3
    }
}
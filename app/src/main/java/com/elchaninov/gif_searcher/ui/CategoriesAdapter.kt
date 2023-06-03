package com.elchaninov.gif_searcher.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.Category
import com.elchaninov.gif_searcher.model.asCategory


class CategoriesAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val categoryList: MutableList<Category> = mutableListOf()

    fun setItems(categories: Collection<Category>) {
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }

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
                    onClick = {
                        if (categoryList[position].subcategories.isEmpty()) {
                            onItemClickListener.onItemClick(it)
                        } else {
                            if (it.isExpanded) collapseCategory(it) else expandCategory(it)
                        }
                    }
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

    private fun expandCategory(category: Category) {
        categoryList.indexOf(category).let {
            val oldCategory = categoryList[it]
            categoryList[it] = oldCategory.copy(isExpanded = !oldCategory.isExpanded)

            val subcategoryList = mutableListOf<Category>()
            categoryList[it].subcategories.map { subcategory ->
                subcategoryList.add(subcategory.asCategory())
            }

            categoryList.addAll(it + 1, subcategoryList)
            notifyDataSetChanged()
        }
    }

    private fun collapseCategory(category: Category) {
        categoryList.indexOf(category).let {
            val oldCategory = categoryList[it]
            categoryList[it] = oldCategory.copy(isExpanded = !oldCategory.isExpanded)

            oldCategory.subcategories.forEach { subcategory ->
                val index = categoryList.indexOfFirst { category ->
                    category.name == subcategory.nameEncoded
                }
                categoryList.removeAt(index)
            }
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    companion object {
        const val TRENDING_CATEGORIES_VIEW_TYPE = 0
        const val COLLAPSE_CATEGORIES_VIEW_TYPE = 1
        const val EXPANDED_CATEGORIES_VIEW_TYPE = 2
        const val SUBCATEGORIES_VIEW_TYPE = 3
    }
}
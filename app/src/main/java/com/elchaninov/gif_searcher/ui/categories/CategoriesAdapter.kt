package com.elchaninov.gif_searcher.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.TypedCategory


class CategoriesAdapter(
    private val onItemClickListener: (TypedCategory) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val categoryList: MutableList<TypedCategory> = mutableListOf()

    fun setItems(categories: List<TypedCategory>) {
        categoryList.clear()
        categoryList.addAll(categories)
    }

    fun getItems(): List<TypedCategory> = categoryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            COLLAPSE_CUSTOM_CATEGORIES_VIEW_TYPE, EXPANDED_CUSTOM_CATEGORIES_VIEW_TYPE -> {
                val itemView = inflater.inflate(R.layout.custom_category_item, parent, false)
                val layoutParams =
                    StaggeredGridLayoutManager.LayoutParams(itemView.layoutParams).apply {
                        isFullSpan = viewType == EXPANDED_CUSTOM_CATEGORIES_VIEW_TYPE
                    }
                itemView.layoutParams = layoutParams
                CustomCategoryViewHolder(itemView)
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
                SubcategoryViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> {
                (categoryList[position] as? TypedCategory.Category)?.let {
                    holder.bind(it, onItemClickListener)
                }
            }
            is CustomCategoryViewHolder -> {
                (categoryList[position] as? TypedCategory.Custom)?.let {
                    holder.bind(it, onItemClickListener)
                }
            }
            is SubcategoryViewHolder -> {
                (categoryList[position] as? TypedCategory.Subcategory)?.let {
                    holder.bind(it, onItemClickListener)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (categoryList[position]) {
            is TypedCategory.Custom -> {
                if ((categoryList[position] as TypedCategory.Custom).isExpanded) EXPANDED_CUSTOM_CATEGORIES_VIEW_TYPE
                else COLLAPSE_CUSTOM_CATEGORIES_VIEW_TYPE
            }
            is TypedCategory.Category -> {
                if ((categoryList[position] as TypedCategory.Category).isExpanded) EXPANDED_CATEGORIES_VIEW_TYPE
                else COLLAPSE_CATEGORIES_VIEW_TYPE
            }
            is TypedCategory.Subcategory -> SUBCATEGORIES_VIEW_TYPE
        }
    }

    companion object {
        const val COLLAPSE_CUSTOM_CATEGORIES_VIEW_TYPE = 0
        const val EXPANDED_CUSTOM_CATEGORIES_VIEW_TYPE = 1
        const val COLLAPSE_CATEGORIES_VIEW_TYPE = 2
        const val EXPANDED_CATEGORIES_VIEW_TYPE = 3
        const val SUBCATEGORIES_VIEW_TYPE = 4
    }
}
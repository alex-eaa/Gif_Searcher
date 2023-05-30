package com.elchaninov.gif_searcher.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elchaninov.gif_searcher.R
import com.elchaninov.gif_searcher.model.Category


class CategoriesAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var categoryList: MutableList<Category> = mutableListOf()

    fun setItems(categories: Collection<Category>) {
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TRENDING_CATEGORIES_VIEW_TYPE) {
            val view = inflater.inflate(R.layout.trending_category_item, parent, false)
            TrendingCategoryViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.category_item, parent, false)
            CategoryViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> {
                holder.bind(categoryList[position], onItemClickListener)
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
        return if (categoryList[position].name == null) TRENDING_CATEGORIES_VIEW_TYPE
        else CATEGORIES_VIEW_TYPE
    }


    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    companion object {
        const val TRENDING_CATEGORIES_VIEW_TYPE = 0
        const val CATEGORIES_VIEW_TYPE = 1
    }
}
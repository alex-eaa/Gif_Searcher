package com.elchaninov.gif_searcher.ui.categories

import androidx.recyclerview.widget.DiffUtil
import com.elchaninov.gif_searcher.model.Category

class CategoriesDiffUtilCallback(
    private val oldList: List<Category>,
    private val newList: List<Category>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCategory: Category = oldList[oldItemPosition]
        val newCategory: Category = newList[newItemPosition]
        return oldCategory.name == newCategory.name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCategory: Category = oldList[oldItemPosition]
        val newCategory: Category = newList[newItemPosition]
        return (oldCategory.name == newCategory.name && oldCategory.isExpanded == newCategory.isExpanded)
    }
}
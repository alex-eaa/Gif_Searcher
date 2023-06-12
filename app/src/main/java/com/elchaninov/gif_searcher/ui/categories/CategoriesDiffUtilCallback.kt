package com.elchaninov.gif_searcher.ui.categories

import androidx.recyclerview.widget.DiffUtil
import com.elchaninov.gif_searcher.model.TypedCategory

class CategoriesDiffUtilCallback(
    private val oldList: List<TypedCategory>,
    private val newList: List<TypedCategory>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTypedCategory: TypedCategory = oldList[oldItemPosition]
        val newTypedCategory: TypedCategory = newList[newItemPosition]
        return oldTypedCategory == newTypedCategory
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTypedCategory: TypedCategory = oldList[oldItemPosition]
        val newTypedCategory: TypedCategory = newList[newItemPosition]
        return (oldTypedCategory is TypedCategory.Category
                && newTypedCategory is TypedCategory.Category
                && oldTypedCategory.name == newTypedCategory.name
                && oldTypedCategory.isExpanded == newTypedCategory.isExpanded)
    }
}
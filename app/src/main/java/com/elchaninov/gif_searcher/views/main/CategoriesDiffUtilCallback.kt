package com.elchaninov.gif_searcher.views.main

import androidx.recyclerview.widget.DiffUtil
import com.elchaninov.gif_searcher.model.data.userdata.TypedCategory

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
        return oldTypedCategory.name == newTypedCategory.name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTypedCategory: TypedCategory = oldList[oldItemPosition]
        val newTypedCategory: TypedCategory = newList[newItemPosition]
        return if (oldTypedCategory is TypedCategory.Custom && newTypedCategory is TypedCategory.Custom)
            oldTypedCategory.isExpanded == newTypedCategory.isExpanded
        else if (oldTypedCategory is TypedCategory.Category && newTypedCategory is TypedCategory.Category)
            oldTypedCategory.isExpanded == newTypedCategory.isExpanded
        else true
    }
}
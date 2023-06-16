package com.elchaninov.gif_searcher.views.main

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class CustomItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as? StaggeredGridLayoutManager
        val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanCount = layoutManager?.spanCount ?: 1
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount?.let {
            it - 1
        }

        val isFullSpan = layoutParams.isFullSpan
        val column = layoutParams.spanIndex
        val isLastColumn = column == spanCount - 1

        outRect.top = space
        outRect.left = space

        if (isFullSpan) {
            outRect.right = space
        } else if (isLastColumn) {
            outRect.right = space
        }

        if (position == itemCount) {
            outRect.bottom = space
        }
    }
}
package com.elchaninov.gif_searcher.ui.categories

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
//        val position = parent.getChildAdapterPosition(view)

        val isFullSpan = layoutParams.isFullSpan
        val column = layoutParams.spanIndex
//        val isFirstColumn = column == 0
        val isLastColumn = column == spanCount - 1

        outRect.left = space
        outRect.bottom = space

        if (isFullSpan) {
            outRect.top = space
            outRect.right = space
        } else if (isLastColumn) {
            outRect.right = space
        }
    }
}
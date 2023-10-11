package com.cthacademy.android.custom

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingItemDecoration(private val verticalSpacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // Apply vertical spacing to all items except the first one
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = verticalSpacing
        }
    }
}
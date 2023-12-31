package com.example.madcamp_week1_rev

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
class GalleryImageSpacing(private val space: Int): RecyclerView.ItemDecoration()  {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.top = space
    }
}
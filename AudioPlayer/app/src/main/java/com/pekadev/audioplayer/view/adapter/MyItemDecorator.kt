package com.pekadev.audioplayer.view.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MyItemDecoration(val offset: Int) : RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = offset/2
        outRect.left = offset
        outRect.right = offset
        outRect.bottom = offset/2

    }
}
package com.cztang.riskguard.UiUtil

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import pokercc.android.expandablerecyclerview.ExpandableAdapter
import pokercc.android.expandablerecyclerview.ExpandableRecyclerView

class DeviceItemDecoration : RecyclerView.ItemDecoration() {
    private val linePaint = Paint().apply {
        color = 0xffA6A6AF.toInt()
        strokeWidth = 1.dpToPx()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        parent as ExpandableRecyclerView
        val adapter = parent.requireAdapter()
        val layoutManager = parent.layoutManager!!

        val leftPadding = 15.dpToPx()
        val rightPadding = 15.dpToPx()

        for (view in parent) {
            val viewHolder = parent.getChildViewHolder(view)
            val params = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams
            viewHolder as ExpandableAdapter.ViewHolder
            val (groupPosition, childPosition) = adapter.getItemLayoutPosition(viewHolder)
            val childCount = adapter.getChildCount(groupPosition)
            if (!adapter.isGroup(viewHolder.itemViewType) && childPosition != childCount - 1) {
                val y = layoutManager.getDecoratedBottom(view) + view.translationY
                parent.clipAndDrawChild(c, view) {
                    it.drawLine(
                        parent.paddingStart + leftPadding + params.marginStart,
                        y,
                        parent.width - parent.paddingEnd.toFloat() - rightPadding - params.marginEnd,
                        y,
                        linePaint
                    )
                }
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        parent as ExpandableRecyclerView
        val adapter = parent.requireAdapter()
        val viewHolder = parent.getChildViewHolder(view)

        val isGroup = adapter.isGroup(viewHolder.itemViewType)
        val firstChild = {
            adapter.getItemLayoutPosition(viewHolder as ExpandableAdapter.ViewHolder).childPosition == 0
        }
        if (isGroup || firstChild()) {
            outRect.top = 12.dpToPx().toInt()
        }
    }
}
package com.danilodequeiroz.notes.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.danilodequeiroz.notes.R
private const val SWIPE_THRESHOLD = 0.7f
private const val ZERO = 0f
abstract class SwipeToDeleteCallback(context: Context) : ItemTouchHelper.Callback() {

    private val clearPaint = Paint()
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#b80f0a")
    private val deleteDrawable: Drawable?
    private val intrinsicWidth: Int
    private val intrinsicHeight: Int


    init {
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete)
        intrinsicWidth = deleteDrawable?.intrinsicWidth ?: ZERO.toInt()
        intrinsicHeight = deleteDrawable?.intrinsicHeight ?: ZERO.toInt()
    }


    override fun getMovementFlags(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(ZERO.toInt(), ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        viewHolder1: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val itemHeight = itemView.height

        val isCancelled = dX == ZERO && !isCurrentlyActive

        if (isCancelled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        deleteDrawable?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteDrawable?.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float?, top: Float?, right: Float?, bottom: Float?) {
        c.drawRect(left ?: ZERO, top ?: ZERO, right ?: ZERO, bottom ?: ZERO, clearPaint)

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return SWIPE_THRESHOLD
    }
}
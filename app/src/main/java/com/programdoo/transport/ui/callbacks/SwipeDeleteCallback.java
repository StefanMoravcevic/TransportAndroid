package com.programdoo.transport.ui.callbacks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.R;

abstract public class SwipeDeleteCallback extends ItemTouchHelper.Callback {
    Context context;
    private Paint clearPaint;
    private ColorDrawable background;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;

    public SwipeDeleteCallback(Context context) {
        this.context = context;
        this.background = new ColorDrawable();
        this.backgroundColor = ContextCompat.getColor(context, R.color.error);
        this.clearPaint = new Paint();
        this.clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.deleteDrawable = ContextCompat.getDrawable(context, R.drawable.icon_trash);
        this.deleteDrawable.setTint(ContextCompat.getColor(context, R.color.white));
        this.intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        this.intrinsicHeight = deleteDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder1,
            @NonNull RecyclerView.ViewHolder viewHolder2) {
        // ne zelimo draggovanje
        return false;
    }

    @Override
    public void onChildDraw(
            @NonNull Canvas canvas,
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            float dx, float dy, int actionState,
            boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dx == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(canvas, itemView.getRight() + dx,
                    (float) itemView.getTop(),
                    (float) itemView.getRight(),
                    (float) itemView.getBottom());
            super.onChildDraw(canvas, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);
            return;
        }

        background.setColor(backgroundColor);
        background.setBounds(itemView.getRight() + (int) dx,
                itemView.getTop(),
                itemView.getRight(), itemView.getBottom());
        background.draw(canvas);

        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteDrawable.draw(canvas);
        super.onChildDraw(canvas, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

    private void clearCanvas(@NonNull Canvas canvas, Float left, Float top, Float right, Float bottom) {
        canvas.drawRect(left, top, right, bottom, clearPaint);

    }
}

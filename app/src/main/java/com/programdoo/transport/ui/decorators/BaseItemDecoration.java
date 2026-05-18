package com.programdoo.transport.ui.decorators;

import android.content.Context;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;
/**
 * ItemDecoration je objekat koji se registruje u recycler view i govori mu
 * kako da dekorise svaki item.
 */
public abstract class BaseItemDecoration extends RecyclerView.ItemDecoration {
    protected final Paint paint = new Paint();
    protected int lineHeight;
    public BaseItemDecoration(
            Context context,
            int color,
            int heightDp) {
        super();
        paint.setColor(color);
        float fLineHeight = (context.getResources().getDisplayMetrics().density * heightDp);
        paint.setStrokeWidth(fLineHeight);
        this.lineHeight = (int) fLineHeight;
    }
}

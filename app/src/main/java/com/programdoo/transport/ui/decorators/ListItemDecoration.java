package com.programdoo.transport.ui.decorators;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.R;

/**
 * crta divider liniju ispod svakog itema osim poslednjeg
 */
public class ListItemDecoration extends BaseItemDecoration {
    protected int hPadding;
    public ListItemDecoration(
            Context context,
            int color,
            int heightDp) {
        super(context, color, heightDp);
        hPadding = context.getResources().getDimensionPixelSize(R.dimen.itemContainerPad);
    }

    @Override
    public void onDrawOver(
            @NonNull Canvas canvas,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state) {
        /*
          child je view unutar recycler view-a. to je ono sto se zapravo iscrta i vidi na ekranu. <br>
          item predstavlja entity koji se nalazi iza view-a i koji ga puni informacijama. <br>
          ako imamo 100 itema u listi, a na ekranu se vidi 10, to znaci da trenutni recycler view
          ima 10 child-ova. */
        int childCount = parent.getChildCount();
        int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;
        // ovaj loop definise gde se sve crta dekoracija
        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            // preskoci poslednji item, ne treba mu divider
            if (position == itemCount - 1) continue;
            // ova linija definise kako se crta dekoracija. postoje i drawArc, drawBitmap...
            canvas.drawLine(
                    child.getLeft() + hPadding, child.getBottom(),
                    child.getRight() - hPadding, child.getBottom(),
                    paint);
        }
    }
}

package com.programdoo.transport.ui.callbacks;

import android.view.View;

/**
 * click listener za adapter
 * @param <T> entity u adapteru
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view, int position, T item);
}

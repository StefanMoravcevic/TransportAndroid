package com.programdoo.transport.ui.callbacks;

import android.view.View;
/**
 * long click listener za adapter
 * @param <T> entity u adapteru
 */
public interface OnItemLongClickListener<T> {
    void onItemLongClick(View view, int position, T item);
}


package com.programdoo.transport.ui.adapters;

import android.view.MenuItem;

import androidx.annotation.Nullable;

public final class ToolbarAction {
    public final int id;
    public final int iconRes;
    public final int titleRes;
    public final int showAsAction;
    public final int tintColor;

    @Nullable
    public final MenuItemHandler handler;
    @Nullable
    public final Runnable onClickHint;
    @Nullable
    public final Runnable onLongClick;

    public ToolbarAction(
            int id,
            int iconRes,
            int titleRes,
            int showAsAction,
            int tintColor,
            MenuItemHandler handler) {
        this(
                id,
                iconRes,
                titleRes,
                showAsAction,
                tintColor,
                handler,
                null,
                null);
    }

    public ToolbarAction(
            int id,
            int iconRes,
            int titleRes,
            int showAsAction,
            int tintColor,
            Runnable onClickHint,
            Runnable onLongClick) {
        this(
                id,
                iconRes,
                titleRes,
                showAsAction,
                tintColor,
                null,
                onClickHint,
                onLongClick);
    }

    public ToolbarAction(
            int id,
            int iconRes,
            int titleRes,
            int showAsAction,
            int tintColor,
            @Nullable MenuItemHandler handler,
            @Nullable Runnable onClickHint,
            @Nullable Runnable onLongClick) {
        this.id = id;
        this.iconRes = iconRes;
        this.titleRes = titleRes;
        this.showAsAction = showAsAction;
        this.tintColor = tintColor;
        this.handler = handler;
        this.onClickHint = onClickHint;
        this.onLongClick = onLongClick;
    }

    public boolean usesActionView() {
        return onClickHint != null || onLongClick != null;
    }

    public interface MenuItemHandler {
        boolean onClick(MenuItem item);
    }
}

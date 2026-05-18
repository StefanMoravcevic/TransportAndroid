package com.programdoo.transport.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.util.List;

public class MultiSelectDialog extends SelectBase {
    public MultiSelectDialog(
            Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        multi = true;
    }
    public void addSelected(int position) {
        int id = adapter.getSelectedItemId(position);
        String description = adapter.getSelectedItemDescription(position);
        adapter.addSelected(id, description);
    }

    public void addSelected(List<Integer> ids, List<String> names) {
        adapter.addSelected(ids, names);
    }

    public List<Integer> getSelectedIds() {
        return adapter.getSelectedIds();
    }

    @Override
    protected void updateDescription(List<String> descriptionList) {
        StringBuilder sb = new StringBuilder();
        for (String description: descriptionList)
            sb.append(description).append(", ");

        String description = sb.toString();
        this.et.setText(description);
    }
}

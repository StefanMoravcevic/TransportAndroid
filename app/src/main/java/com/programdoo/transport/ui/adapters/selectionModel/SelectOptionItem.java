package com.programdoo.transport.ui.adapters.selectionModel;

import lombok.Getter;

public final class SelectOptionItem {
    @Getter
    private final int id;
    @Getter
    private final String description;

    public SelectOptionItem(int id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectOptionItem)) return false;
        return id == ((SelectOptionItem) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

package com.programdoo.transport.data.models.enums;

import androidx.annotation.NonNull;

import com.programdoo.transport.utils.StringUtil;

import lombok.Getter;

public enum Languages implements BaseEnum {
    EN_US(1, "en-US"),
    SR_LATN(2, "sr-Latn"),
    SR_CYRL(3, "sr-Cyrl");

    @Getter
    private final int value;
    @Getter
    private final String description;

    Languages(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @NonNull
    public String toString() {
        return description;
    }

    public static Languages fromId(Integer id) {
        if (id == null) return null;
        for (Languages l : values()) {
            if (l.value == id) return l;
        }
        throw new IllegalArgumentException("Invalid language id: " + id);
    }

    public static Languages fromDescription(String description) {
        if (StringUtil.isNullOrEmpty(description)) return null;
        for (Languages l: values()) {
            if (l.description.equals(description)) return l;
        }
        throw new IllegalArgumentException("Invalid language description: " + description);
    }
}

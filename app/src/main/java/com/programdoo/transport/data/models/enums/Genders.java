package com.programdoo.transport.data.models.enums;

import com.programdoo.transport.utils.StringUtil;

import lombok.Getter;

public enum Genders implements BaseEnum {
    MALE(1, "Male"),
    FEMALE(2, "Female");

    @Getter
    private final int value;
    @Getter
    private final String description;

    Genders(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public String toString() {
        return description;
    }

    public static Genders fromValue(Integer id) {
        if (id == null) return null;
        for (Genders g: values()) {
            if (g.value == id) return g;
        }

        throw new IllegalArgumentException("Invalid Gender id: " + id);
    }

    public static Genders fromDescription(String description) {
        if (StringUtil.isNullOrEmpty(description)) return null;
        for (Genders g: values()) {
            if (g.description.equals(description)) return g;
        }
        throw new IllegalArgumentException("Invalid gender description: " + description);
    }
}

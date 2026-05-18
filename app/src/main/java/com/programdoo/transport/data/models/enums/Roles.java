package com.programdoo.transport.data.models.enums;

import com.programdoo.transport.utils.StringUtil;

import lombok.Getter;
import lombok.NonNull;

public enum Roles implements BaseEnum {
    ADMIN(1, "Administrator"),
    TRAINER(10, "Trainer"),
    TRAINEE(11, "Trainee"),
    SUPERVISOR(12, "Supervisor");

    @Getter
    private final int value;
    @Getter
    private final String description;

    Roles(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @NonNull
    public String toString() {
        return description;
    }

    public static Roles fromId(Integer id) {
        if (id == null) return null;
        for (Roles r: values()) if (r.value == id) return r;
        throw new IllegalArgumentException("Invalid role id: " + id);
    }

    public static Roles fromDescription(String description) {
        if (StringUtil.isNullOrEmpty(description)) return null;
        for (Roles r: values()) if (r.description.equals(description)) return r;
        throw new IllegalArgumentException("Invalid role description: " + description);
    }
}

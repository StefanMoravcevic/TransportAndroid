package com.programdoo.transport.data.models.enums;

import com.programdoo.transport.utils.StringUtil;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

import lombok.Getter;

public enum Weekdays implements BaseEnum {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    @Getter
    private final int value;

    public String getDescription() {
        return DayOfWeek.of(value + 1).getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }
    public String getDescription(Locale locale) {
        return DayOfWeek.of(value + 1).getDisplayName(TextStyle.SHORT, locale);
    }

    Weekdays(int value) {
        this.value = value;
    }

    public static Weekdays fromValue(Integer id) {
        if (id == null) return null;
        for (Weekdays w: values()) {
            if (w.value == id) return w;
        }

        throw new IllegalArgumentException("Invalid Weekday id: " + id);
    }

    public static Weekdays fromDescription(String description, Locale locale) {
        if (StringUtil.isNullOrEmpty(description)) return null;
        for (Weekdays w: values()) {
            if (w.getDescription(locale).equals(description)) return w;
        }

        throw new IllegalArgumentException("Invalid Weekday description: " + description);
    }
}

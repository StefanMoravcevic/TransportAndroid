package com.programdoo.transport.utils;

public class StringUtil {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }
    public static String toString(Integer value) {
        if (value == null) return "";
        else return value.toString();
    }
    public static String normalizeForUi(String unknownLabel, String value) {
        return value == null
                ? unknownLabel
                : value;
    }
}

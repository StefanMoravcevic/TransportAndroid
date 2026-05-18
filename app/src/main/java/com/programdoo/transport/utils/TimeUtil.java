package com.programdoo.transport.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static final DateTimeFormatter API_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter CLIENT_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    public static String apiFormat(LocalTime input) {
        return input != null ? input.format(API_FORMATTER) : null;
    }
    public static String format(LocalTime input) {
        return input != null ? input.format(CLIENT_FORMATTER) : "";
    }

    public static LocalTime apiParse(String input) {
        return !StringUtil.isNullOrEmpty(input) ? LocalTime.parse(input, API_FORMATTER) : null;
    }

    public static LocalTime parse(String input) {
        return !StringUtil.isNullOrEmpty(input) ? LocalTime.parse(input, CLIENT_FORMATTER) : null;
    }
}

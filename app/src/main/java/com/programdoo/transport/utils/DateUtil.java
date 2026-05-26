package com.programdoo.transport.utils;

import com.programdoo.transport.data.models.enums.Weekdays;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateUtil {

    private static final DateTimeFormatter FLEXIBLE_API_FORMAT =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .optionalStart()
                    .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                    .optionalEnd()
                    .toFormatter();

    private static final DateTimeFormatter API_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static final DateTimeFormatter CLIENT_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static final DateTimeFormatter UI_DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static String apiFormat(LocalDateTime input) {
        if (input == null) return null;
        return input.withNano(0).format(API_FORMAT);
    }

    public static String format(LocalDateTime input) {
        return input != null ? input.format(CLIENT_FORMATTER) : "";
    }
    private static final DateTimeFormatter API_OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static LocalDateTime apiParse(String input) {
        if (StringUtil.isNullOrEmpty(input)) return null;

        try {
            return LocalDateTime.parse(input, FLEXIBLE_API_FORMAT);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Ne mogu da parsiram datum: " + input, e);
        }
    }

    public static LocalDateTime parse(String input) {
        if (StringUtil.isNullOrEmpty(input)) return null;
        LocalDate date = LocalDate.parse(input, CLIENT_FORMATTER);
        return date.atStartOfDay();
    }

    public static List<String> daysOfWeek() {
        return IntStream.range(0, 7)
                .mapToObj(i -> Weekdays.fromValue(i)
                        .getDescription())
                .collect(Collectors.toList());
    }

    public static LocalDateTime parseClientDateTime(String input) {
        if (input == null || input.isEmpty()) return null;
        return LocalDateTime.parse(input, UI_DATETIME_FORMAT);
    }

    public static String apiFormatNew(LocalDateTime input) {
        if (input == null) return null;

        return input
                .withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
}

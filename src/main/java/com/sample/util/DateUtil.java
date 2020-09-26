package com.sample.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static com.sample.util.PlatformUtils.RU_LOCALE;
import static java.time.format.DateTimeFormatter.ofPattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {
    public static final long MILLISECOND = 1_000_000;

    static final String FULL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final DateTimeFormatter FULL_DATE_TIME_FORMATTER =
            ofPattern(FULL_DATE_TIME_PATTERN, RU_LOCALE);
    private static final DateTimeFormatter DATE_FORMATTER =
            ofPattern(DATE_PATTERN, RU_LOCALE);

    public static String format(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.format(FULL_DATE_TIME_FORMATTER);
    }

    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }

        return date.format(DATE_FORMATTER);
    }

    public static OffsetDateTime toDateTime(String string) {
        if (StringUtils.isEmpty(string)) {
            return null;
        }

        return OffsetDateTime.from(FULL_DATE_TIME_FORMATTER.parse(string));
    }

    public static LocalDate toDate(String string) {
        if (StringUtils.isEmpty(string)) {
            return null;
        }

        return LocalDate.from(DATE_FORMATTER.parse(string));
    }

    public static OffsetDateTime removeNanoseconds(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.withNano(0);
    }

    public static long toMillis(long nanoseconds) {
        return (long) (nanoseconds / (MILLISECOND * 1.0));
    }
}
package com.sample.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static com.sample.util.PlatformUtils.ZONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DateUtilTest {
    private static Stream<Arguments> format() {
        return Stream.of(
                arguments(null, null),
                arguments(OffsetDateTime.of(2020, 6, 15,
                        22, 57, 58,
                        0, ZONE), "2020-06-15T22:57:58.000+0000")
        );
    }

    @ParameterizedTest
    @MethodSource("format")
    void format(OffsetDateTime source, String expected) {
        assertEquals(expected, DateUtil.format(source));
    }

    private static Stream<Arguments> formatLocalDate() {
        return Stream.of(
                arguments(null, null),
                arguments(LocalDate.of(2020, 6, 15), "2020-06-15")
        );
    }

    @ParameterizedTest
    @MethodSource("formatLocalDate")
    void formatLocalDate(LocalDate source, String expected) {
        assertEquals(expected, DateUtil.format(source));
    }

    @ParameterizedTest
    @MethodSource("format")
    void toDateTime(OffsetDateTime expected, String source) {
        assertEquals(expected, DateUtil.toDateTime(source));
    }

    @ParameterizedTest
    @MethodSource("formatLocalDate")
    void toDate(LocalDate expected, String source) {
        assertEquals(expected, DateUtil.toDate(source));
    }

    private static Stream<Arguments> removeNanoseconds() {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2020, 6, 15,
                22, 57, 58,
                123456, ZONE);
        OffsetDateTime withoutNanoseconds = offsetDateTime.withNano(0);

        return Stream.of(
                arguments(null, null),
                arguments(offsetDateTime, withoutNanoseconds)
        );
    }

    @ParameterizedTest
    @MethodSource("removeNanoseconds")
    void removeNanoseconds(OffsetDateTime source, OffsetDateTime expected) {
        assertEquals(expected, DateUtil.removeNanoseconds(source));
    }

    private static Stream<Arguments> toMillis() {
        return Stream.of(
                arguments(0, 0),
                arguments(1_000_000, 1),
                arguments(10_000_000, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("toMillis")
    void toMillis(long source, long expected) {
        assertEquals(expected, DateUtil.toMillis(source));
    }
}
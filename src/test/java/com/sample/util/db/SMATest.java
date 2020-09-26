package com.sample.util.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SMATest {
    private static final long DEFAULT_VALUE = 10L;

    private static Stream<Arguments> periodProvider() {
        return Stream.of(
                arguments(0),
                arguments(-1),
                arguments(-10)
        );
    }

    @ParameterizedTest
    @MethodSource("periodProvider")
    void testPeriod(int period) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new SMA(period);
        });
    }

    private static Stream<Arguments> avgProvider() {
        return Stream.of(
                arguments(Arrays.asList(1L, 2L, 3L, 4L, 5L,
                        10L, 10L, 10L, 10L, 10L,
                        10L, 10L, 10L, 10L, 10L), 10L),
                arguments(Arrays.asList(1L, 2L, 3L, 4L, 5L), 3L),
                arguments(Arrays.asList(1L, 2L, 3L), 2L),
                arguments(Arrays.asList(3L, 3L), 3L),
                arguments(Arrays.asList(), DEFAULT_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("avgProvider")
    void testAvg(List<Long> values, Long expected) {
        SMA sma = new SMA(10);

        for (Long value : values) {
            sma.add(value);
        }

        assertEquals((long) expected, sma.avg(DEFAULT_VALUE));
    }

    @Test
    void testMax() {
        final int period = 10;

        SMA sma = new SMA(period);

        for (long i = 0; i < 15; i++) {
            sma.add(i);
        }

        assertEquals(9, sma.avg(DEFAULT_VALUE));
        assertEquals(period, sma.size());
    }
}
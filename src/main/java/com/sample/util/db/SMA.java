package com.sample.util.db;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple Moving Average (SMA).
 * https://www.investopedia.com/terms/s/sma.asp
 */
public class SMA {
    private final List<Long> items;

    /**
     * Fixed size.
     */
    private final int period;

    public SMA() {
        this(10);
    }

    public SMA(int period) {
        if (period <= 0) {
            throw new IllegalArgumentException("Period should be positive value '" + period + "'");
        }

        this.items = new CopyOnWriteArrayList<>();
        this.period = period;
    }

    public void add(Long value) {
        items.add(value);

        if (items.size() > period) {
            items.remove(0);
        }
    }

    public long avg(long defaultValue) {
        if (CollectionUtils.isEmpty(items)) {
            return defaultValue;
        }

        OptionalDouble average = items.stream().mapToLong(x -> x).average();

        if (average.isPresent()) {
            return (long) average.getAsDouble();
        }

        return defaultValue;
    }

    public int size() {
        return items.size();
    }
}

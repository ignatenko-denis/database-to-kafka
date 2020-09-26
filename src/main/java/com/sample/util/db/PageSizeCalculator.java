package com.sample.util.db;

import com.sample.config.AppConfig;
import com.sample.entity.TradeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static com.sample.entity.TradeType.SCHEDULED;
import static com.sample.util.DateUtil.MILLISECOND;
import static com.sample.util.DateUtil.toMillis;

@Slf4j
@Component
public class PageSizeCalculator {
    static final int DEFAULT_SIZE = 10;
    static final long DEFAULT_TIME_FOR_ROW = 100 * MILLISECOND;

    private final Map<TradeType, CalculatorItem> map = new HashMap<>();

    @Autowired
    private AppConfig config;

    @PostConstruct
    public void init() {
        map.putIfAbsent(SCHEDULED, new CalculatorItem(config.getPeriod()));
    }

    public void add(long nanoseconds, int amount, TradeType type) {
        if (amount <= 0) {
            return;
        }

        CalculatorItem calculatorItem = map.get(type);

        calculatorItem.getTimeForRow().add((long) (nanoseconds / (amount * 1.0)));
    }

    public int calcPageSize(int rateMilliseconds, TradeType type) {
        if (rateMilliseconds <= 0) {
            return DEFAULT_SIZE;
        }

        long rateNanoseconds = rateMilliseconds * MILLISECOND;
        long avgTimeForRow = avg(type);

        int result = (int) (rateNanoseconds / (avgTimeForRow * 1.0));

        log.info("calcPageSize {}, avg (millis) {}", result, toMillis(avgTimeForRow));

        return result;
    }

    long avg(TradeType type) {
        CalculatorItem calculatorItem = map.get(type);

        return calculatorItem.getTimeForRow().avg(DEFAULT_TIME_FOR_ROW);
    }

    int getSize(TradeType type) {
        CalculatorItem calculatorItem = map.get(type);

        return calculatorItem.getTimeForRow().size();
    }
}

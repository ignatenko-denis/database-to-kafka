package com.sample.util.db;

import lombok.Getter;

public class CalculatorItem {
    /**
     * Average time spends on processing one row (in milliseconds).
     */
    @Getter
    private final SMA timeForRow;

    public CalculatorItem(int period) {
        this.timeForRow = new SMA(period);
    }
}

package com.sample.util.db;

import com.sample.config.AppConfig;
import com.sample.entity.TradeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.sample.entity.TradeType.SCHEDULED;
import static com.sample.util.DateUtil.MILLISECOND;
import static com.sample.util.db.PageSizeCalculator.DEFAULT_SIZE;
import static com.sample.util.db.PageSizeCalculator.DEFAULT_TIME_FOR_ROW;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

class PageSizeCalculatorTest {
    private static final TradeType TYPE = SCHEDULED;

    @Mock
    private AppConfig config;

    @InjectMocks
    private PageSizeCalculator calculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(config.getPeriod()).thenReturn(10);

        calculator.init();
    }

    @Test
    void testSimple() {
        simple(DEFAULT_SIZE, 100);
    }

    @Test
    void testSimpleRate() {
        simple(100, 1000);
    }

    private void simple(int expectedPageSize, int rate) {
        calculator.add(100 * MILLISECOND, 10, TYPE);
        calculator.add(200 * MILLISECOND, 20, TYPE);
        calculator.add(300 * MILLISECOND, 30, TYPE);

        assertEquals(10_000_000, calculator.avg(TYPE));
        assertEquals(expectedPageSize, calculator.calcPageSize(rate, TYPE));
    }

    @Test
    void testComplex() {
        complex(1, 10);
    }

    @Test
    void testComplexRate() {
        complex(142, 1000);
    }

    private void complex(int expectedPageSize, int rate) {
        calculator.add(10 * MILLISECOND, 10, TYPE);
        calculator.add(20 * MILLISECOND, 2, TYPE);
        calculator.add(50 * MILLISECOND, 5, TYPE);

        assertEquals(7_000_000, calculator.avg(TYPE));
        assertEquals(expectedPageSize, calculator.calcPageSize(rate, TYPE));
    }

    @Test
    void testEmpty() {
        assertEquals(DEFAULT_TIME_FOR_ROW, calculator.avg(TYPE));

        assertEquals(10, calculator.calcPageSize(0, TYPE));

        assertEquals(0, calculator.getSize(TYPE));
    }
}
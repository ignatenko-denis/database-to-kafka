package com.sample.util;

import com.sample.dao.TradeRepository;
import com.sample.entity.Status;
import com.sample.entity.Trade;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.sample.entity.Status.CANCELLED;
import static com.sample.entity.Status.COMPLETED;

@SpringBootTest
@TestPropertySource(locations = "/config/application.yml")
@Slf4j
class DataGenerator {
    private static final int MAX = 10;

    private static final List<String> EXCHANGE =
            Arrays.asList("NYSE", "NASDAQ", "LSE");

    // Companies from Dow Jones Industrial Average (DJIA).
    private static final List<String> TICKER =
            Arrays.asList("AAPL", "AMGN", "AXP", "BA", "CAT", "CRM", "CSCO", "CVX", "DIS", "DOW",
                    "GS", "HD", "HON", "IBM", "INTC", "JNJ", "JPM", "KO", "MCD", "MMM",
                    "MRK", "MSFT", "NKE", "PG", "TRV", "UNH", "V", "VZ", "WBA", "WMT");

    private static final List<Status> STATUS = Arrays.asList(COMPLETED, CANCELLED);

    private static final Random RANDOM = new Random();

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @Disabled
    void generate() {
        for (int i = 0; i < MAX; i++) {
            List<Trade> list = new ArrayList<>();

            for (int j = 0; j < 1000; j++) {
                Trade item = new Trade();

                item.setClientId(nextLong(100_000));
                item.setExchange(getRandom(EXCHANGE));
                item.setTicker(getRandom(TICKER));
                item.setPrice(nextPrice());
                item.setAmount(nextLong(100) + 1);
                item.setDate(OffsetDateTime.now());
                item.setStatus(getRandom(STATUS));
                item.setSent(false);

                list.add(item);
            }

            tradeRepository.saveAll(list);
            list.clear();
            log.info("save part {}", i + 1);
        }
    }

    private static <T> T getRandom(List<T> list) {
        int pos = nextInt(list.size());
        return list.get(pos);
    }

    private static int nextInt(int bound) {
        return Math.abs(RANDOM.nextInt(bound));
    }

    private static long nextLong(int bound) {
        return Math.abs(RANDOM.nextLong()) % bound;
    }

    private static BigDecimal nextPrice() {
        double value = Math.abs(RANDOM.nextDouble() * 300 + 1);

        return new BigDecimal("" + value).setScale(2, RoundingMode.HALF_EVEN);
    }
}

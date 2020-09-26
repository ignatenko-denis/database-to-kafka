package com.sample.dao;

import com.sample.entity.Trade;
import com.sample.entity.TradeType;
import com.sample.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.sample.entity.Status.COMPLETED;
import static com.sample.entity.TradeType.SCHEDULED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@TestPropertySource(locations = "/config/application.yml")
@Slf4j
public class TradeTest {
    @Autowired
    private TradeRepository tradeRepository;

    static Stream<Arguments> typeProvider() {
        return Stream.of(
                arguments(SCHEDULED)
        );
    }

    @Test
    @Transactional
    void findNotSentScheduled() {
        Trade trade = build(SCHEDULED);
        tradeRepository.save(trade);

        Page<Trade> page = tradeRepository.findNotSentScheduled(PageRequest.of(0, 10));

        assertTrue(page.getNumberOfElements() > 0);

        trade = page.get().findFirst().get();
        validate(trade);

        tradeRepository.delete(trade);
    }

    public static Trade build(TradeType type) {
        Trade result = new Trade();

        result.setClientId(1L);
        result.setExchange("NYSE");
        result.setTicker("TSLA");
        result.setPrice(new BigDecimal("348"));
        result.setAmount(10L);
        result.setDate(DateUtil.toDateTime("2020-09-25T10:25:06.000+0300"));
        result.setStatus(COMPLETED);
        result.setSent(false);

        return result;
    }

    private void validate(Trade trade) {
        assertNotNull(trade.getId());
        assertEquals(1L, trade.getClientId());
        assertEquals("NYSE", trade.getExchange());
        assertEquals("TSLA", trade.getTicker());
        assertEquals("348", trade.getPrice().toString());
        assertEquals(10L, trade.getAmount());
        assertEquals("2020-09-25T10:25:06.000+0300", DateUtil.format(trade.getDate()));
        assertEquals(COMPLETED, trade.getStatus());
        assertFalse(trade.isSent());
    }
}
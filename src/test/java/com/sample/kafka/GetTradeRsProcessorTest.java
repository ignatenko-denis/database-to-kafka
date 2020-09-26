package com.sample.kafka;

import com.sample.config.AppConfig;
import com.sample.dao.TradeRepository;
import com.sample.dao.TradeTest;
import com.sample.entity.Trade;
import com.sample.entity.TradeType;
import com.sample.util.db.PageSizeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.sample.entity.TradeType.SCHEDULED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetTradeRsProcessorTest {
    @Mock
    private KafkaTemplate template;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private AppConfig config;

    @Mock
    private PageSizeCalculator pageSizeCalculator;

    @InjectMocks
    private GetTradeRsProcessor processor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void sendBulk() {
        TradeType type = SCHEDULED;

        int size = 1;
        when(config.getJobScheduledRate()).thenReturn(size);
        when(pageSizeCalculator.calcPageSize(size, type)).thenReturn(10);

        List<Trade> list = new ArrayList<>();
        list.add(TradeTest.build(type));
        Page<Trade> page = new PageImpl<>(list);

        when(tradeRepository.findNotSentScheduled(any())).thenReturn(page);

        assertEquals(1, processor.sendBulk());

        verify(tradeRepository, times(1)).findNotSentScheduled(any());
        verify(template, times(1)).send(any(), any(), any());
        verify(tradeRepository, times(1)).saveAll(any());
    }

    @Test
    void getTradeRsTopic() {
        assertNotNull(processor.getTradeRsTopic());
    }
}
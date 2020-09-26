package com.sample.kafka;

import com.google.protobuf.Timestamp;
import com.sample.TradeAPI;
import com.sample.config.AppConfig;
import com.sample.dao.TradeRepository;
import com.sample.entity.Status;
import com.sample.entity.Trade;
import com.sample.util.db.PageSizeCalculator;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

import static com.sample.entity.TradeType.SCHEDULED;

@Slf4j
@Component
public class GetTradeRsProcessor {
    private static final String TOPIC = "TradeRs";

    @Autowired
    private KafkaTemplate template;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private AppConfig config;

    @Autowired
    private PageSizeCalculator pageSizeCalculator;

    @Transactional
    public int sendBulk() {
        // calculate page size for select from table
        int size = pageSizeCalculator.calcPageSize(config.getJobScheduledRate(), SCHEDULED);
        Page<Trade> page = tradeRepository.findNotSentScheduled(PageRequest.of(0, size));

        for (Trade trade : page) {
            TradeAPI.TradeRs rs = build(trade);

            template.send(TOPIC, UUID.randomUUID().toString(), rs.toByteArray());

            // update database
            trade.setSent(true);
        }

        // batch save
        tradeRepository.saveAll(page.toList());

        log.info("Send in {}: {} size", TOPIC, page.getNumberOfElements());

        return page.getNumberOfElements();
    }

    private static TradeAPI.TradeRs build(Trade trade) {
        return TradeAPI.TradeRs.newBuilder()
                .setClientId(trade.getClientId())
                .setExchange(trade.getExchange())
                .setTicker(trade.getTicker())
                .setPrice("" + trade.getPrice())
                .setAmount(trade.getAmount())
                .setDate(buildTimestamp(trade))
                .setStatus(buildStatus(trade))
                .build();
    }

    private static Timestamp buildTimestamp(Trade trade) {
        long millis = trade.getDate().toEpochSecond();

        final int millisInSecond = 1000;
        return Timestamp.newBuilder()
                .setSeconds(millis / millisInSecond)
                .setNanos((int) ((millis % millisInSecond) * 1000_000))
                .build();
    }

    private static TradeAPI.TradeRs.Status buildStatus(Trade trade) {
        if (trade.getStatus() == Status.COMPLETED) {
            return TradeAPI.TradeRs.Status.COMPLETED;
        } else if (trade.getStatus() == Status.CANCELLED) {
            return TradeAPI.TradeRs.Status.CANCELLED;
        }

        return null;
    }

    @Bean
    public NewTopic getTradeRsTopic() {
        return TopicBuilder.name(TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }
}

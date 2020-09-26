package com.sample.job;

import com.sample.config.AppConfig;
import com.sample.kafka.GetTradeRsProcessor;
import com.sample.util.db.DatabaseExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.sample.entity.TradeType.SCHEDULED;

@Slf4j
@Component
public class GetTradeScheduledRsJob {
    @Autowired
    private GetTradeRsProcessor processor;

    @Autowired
    private AppConfig config;

    @Scheduled(cron = "${app.job.scheduled.cron}")
    @DatabaseExecutionTime(SCHEDULED)
    public int job() {
        if (!config.getJobScheduledEnabled()) {
            return 0;
        }

        return processor.sendBulk();
    }
}

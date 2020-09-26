package com.sample.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configuration
@Component
@Getter
@Setter
@Slf4j
@ConfigurationProperties(prefix = "app")
@EnableScheduling
public class AppConfig {
    private String label;

    private Integer period;

    @Value("${app.job.scheduled.enabled}")
    private Boolean jobScheduledEnabled;

    @Value("${app.job.scheduled.rate}")
    private Integer jobScheduledRate;

    @Value("${app.job.scheduled.cron}")
    private String jobScheduledCron;

    @PostConstruct
    private void log() {
        StringBuilder builder = new StringBuilder(300);

        builder.append("Application Configuration:").append("\n")
                .append("\tlabel: ").append(label).append("\n")
                .append("\tperiod: ").append(period).append("\n")
                .append("\tapp.job.scheduled.enabled: ").append(jobScheduledEnabled).append("\n")
                .append("\tapp.job.scheduled.rate: ").append(jobScheduledRate).append("\n")
                .append("\tapp.job.scheduled.cron: ").append(jobScheduledCron).append("\n");

        log.info(builder.toString());
    }
}

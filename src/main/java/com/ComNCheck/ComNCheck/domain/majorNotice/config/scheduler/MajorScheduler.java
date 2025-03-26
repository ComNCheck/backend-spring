package com.ComNCheck.ComNCheck.domain.majorNotice.config.scheduler;

import com.ComNCheck.ComNCheck.domain.majorNotice.service.MajorNoticeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class MajorScheduler {
    private final MajorNoticeService majorNoticeService;

    @Scheduled(cron = "0 0 * * * *")
    public void syncNoticesPeriodically() {
        majorNoticeService.syncMajorNotices();
    }

    @PostConstruct
    public void initLoad() {
        majorNoticeService.syncMajorNotices();
    }
}

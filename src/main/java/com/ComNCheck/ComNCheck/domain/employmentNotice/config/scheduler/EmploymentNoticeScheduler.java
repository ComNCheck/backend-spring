package com.ComNCheck.ComNCheck.domain.employmentNotice.config.scheduler;


import com.ComNCheck.ComNCheck.domain.employmentNotice.service.EmploymentNoticeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class EmploymentNoticeScheduler {

    private final EmploymentNoticeService employmentNoticeService;

    @Scheduled(cron = "0 0 * * * *")
    public void syncEmploymentNoticePeriodically() {
        employmentNoticeService.syncEmploymentNotices();
    }

    @PostConstruct
    public void initLoad() {
        employmentNoticeService.syncEmploymentNotices();
    }
}

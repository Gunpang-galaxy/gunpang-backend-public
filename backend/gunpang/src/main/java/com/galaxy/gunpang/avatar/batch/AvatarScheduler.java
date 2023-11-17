package com.galaxy.gunpang.avatar.batch;


import com.galaxy.gunpang.avatar.batch.AvatarJobConfig;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvatarScheduler {
    private final JobLauncher jobLauncher;
    private final Job dailyJob;

    @Scheduled(cron = "0 0 0 * * *")
    public void daily(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .addLong("time", System.currentTimeMillis()).toJobParameters();

        try{
            jobLauncher.run(dailyJob, jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.galaxy.gunpang.avatar.batch;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AvatarJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AvatarMultiProcessor avatarMultiProcessor;
    private final AvatarLevelProcessor avatarLevelProcessor;
    private final AvatarChunkListener avatarChunkListener;
    private final AvatarJobExecutionListener avatarJobExecutionListener;
    private final EntityManagerFactory entityManagerFactory;

    private final int CHUNK_SIZE = 8;
    private final int POOL_SIZE = 4;

    @Bean(name = "taskPool")
    public TaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(POOL_SIZE);
        executor.setMaxPoolSize(POOL_SIZE);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.setAwaitTerminationSeconds(15);
        executor.initialize();
        return executor;
    }

    @Bean
    public Job dailyJob(){
        return jobBuilderFactory.get("dailyJob")
                .incrementer(new RunIdIncrementer())
                .listener(avatarJobExecutionListener)
                .start(damageStep())
                .next(levelUpStep())
                .build();
    }

    @Bean
    public Step damageStep(){
        return stepBuilderFactory.get("damageStep")
                .<Avatar, Avatar>chunk(CHUNK_SIZE)
                .reader(avatarReader())
                .processor(avatarMultiProcessor)
                .writer(avatarWriter())
                .taskExecutor(executor())
                .throttleLimit(POOL_SIZE)
                .listener(avatarChunkListener)
                .build();
    }

    @Bean
    public Step levelUpStep(){
        return stepBuilderFactory.get("levelUpStep")
                .<Avatar, Avatar>chunk(CHUNK_SIZE)
                .reader(avatarReader())
                .processor(avatarLevelProcessor)
                .writer(avatarWriter())
                .taskExecutor(executor())
                .throttleLimit(POOL_SIZE)
                .listener(avatarChunkListener)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Avatar> avatarReader(){
        return new JpaPagingItemReaderBuilder<Avatar>()
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .queryString("SELECT a FROM Avatar a")
                .saveState(false)
                .build();
    }

    @Bean
    @StepScope
    public JpaItemWriter<Avatar> avatarWriter() {
        return new JpaItemWriterBuilder<Avatar>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}

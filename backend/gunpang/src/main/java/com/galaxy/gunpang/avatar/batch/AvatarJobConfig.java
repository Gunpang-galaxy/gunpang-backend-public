package com.galaxy.gunpang.avatar.batch;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.DeathCause;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

    private final int CHUNK_SIZE = 8;
    private final int POOL_SIZE = 4;

    @Bean(name = "taskPool")
    public TaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(POOL_SIZE);
        executor.setMaxPoolSize(POOL_SIZE);
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

    @Bean(name = "damageJob")
    public Job damageJob(Step damageStep){
        return jobBuilderFactory.get("damageJob")
                .listener(avatarJobExecutionListener)
                .flow(damageStep)
                .end()
                .build();
    }

    @Bean(name = "levelUpJob")
    public Job levelUpJob(Step levelUpStep){
        return jobBuilderFactory.get("levelUpJob")
                .listener(avatarJobExecutionListener)
                .flow(levelUpStep)
                .end()
                .build();
    }

    @Bean
    public Step damageStep(@Qualifier("avatarSyncDamageReader") SynchronizedItemStreamReader<Avatar> avatarDamageReader,
                                     @Qualifier("avatarWriter") ItemWriter<Avatar> avatarWriter){
        return stepBuilderFactory.get("damageStep")
                .<Avatar, Avatar>chunk(CHUNK_SIZE)
                .reader(avatarDamageReader)
                .processor(avatarMultiProcessor)
                .writer(avatarWriter)
                .taskExecutor(executor())
                .throttleLimit(POOL_SIZE)
                .listener(avatarChunkListener)
                .build();
    }

    @Bean
    public Step levelUpStep(@Qualifier("avatarSyncLevelUpReader") SynchronizedItemStreamReader<Avatar> avatarLevelUpReader,
                            @Qualifier("avatarWriter") ItemWriter<Avatar> avatarWriter){
        return stepBuilderFactory.get("levelUpStep")
                .<Avatar, Avatar>chunk(CHUNK_SIZE)
                .reader(avatarLevelUpReader)
                .processor(avatarLevelProcessor)
                .writer(avatarWriter)
                .taskExecutor(executor())
                .throttleLimit(POOL_SIZE)
                .listener(avatarChunkListener)
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<Avatar> avatarSyncDamageReader(AvatarRepository avatarRepository) {
        RepositoryItemReader avatarDamageReader = new RepositoryItemReaderBuilder()
                    .repository(avatarRepository)
                    .methodName("findByStatus")
                    .pageSize(CHUNK_SIZE)
                    .arguments(Arrays.asList(Status.ALIVE))
                    .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                    .name("avatarDamageReader")
                    .build();
        SynchronizedItemStreamReader<Avatar> reader =  new SynchronizedItemStreamReader<Avatar>();
        reader.setDelegate(avatarDamageReader);
        return reader;
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<Avatar> avatarSyncLevelUpReader(AvatarRepository avatarRepository) {
        RepositoryItemReader<Avatar> avatarLevelUpReader = new RepositoryItemReaderBuilder()
                    .repository(avatarRepository)
                    .methodName("getLevelUpAvatars")
                    .pageSize(CHUNK_SIZE)
                    .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                    .name("avatarLevelUpReader")
                    .build();
        SynchronizedItemStreamReader<Avatar> reader = new SynchronizedItemStreamReader<Avatar>();
        reader.setDelegate(avatarLevelUpReader);
        return reader;
    }

    @Bean
    @StepScope
    public ItemWriter<Avatar> avatarWriter(AvatarRepository avatarRepository) {
        return avatars -> avatarRepository.saveAll(avatars);
    }

    @Bean
    @StepScope
    public ItemWriter<DeathCause> deathCauseWriter(DeathCauseRepository deathCauseRepository){
        return deathCauses -> deathCauseRepository.saveAll(deathCauses);
    }

}

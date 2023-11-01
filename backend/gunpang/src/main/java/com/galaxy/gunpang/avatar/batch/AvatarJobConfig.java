package com.galaxy.gunpang.avatar.batch;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.DeathCause;
import com.galaxy.gunpang.avatar.model.enums.Cause;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
import com.galaxy.gunpang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AvatarJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final int CHUNK_SIZE = 10;

    @Bean
    public Job damageJob(Step checkDailyRecordStep){
        return jobBuilderFactory.get("damageJob")
                .flow(checkDailyRecordStep)
                .end()
                .build();
    }

    @Bean
    public Step checkDailyRecordStep(AvatarMultiProcessor avatarMultiProcessor, @Qualifier("avatarReader") RepositoryItemReader<Avatar> avatarReader){
        return stepBuilderFactory.get("checkDailyRecordStep")
                .<Avatar, Avatar>chunk(CHUNK_SIZE)
                .reader(avatarReader)
                .processor(avatarMultiProcessor)
                .writer(avatars -> {})
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Avatar> avatarReader(AvatarRepository avatarRepository){
        return new RepositoryItemReaderBuilder()
                .repository(avatarRepository)
                .methodName("findByStatus")
                .pageSize(CHUNK_SIZE)
                .maxItemCount(CHUNK_SIZE)
                .arguments(Arrays.asList(Status.ALIVE))
                .sorts(Collections.singletonMap("id",Sort.Direction.ASC))
                .name("avatarReader")
                .build();
    }
}

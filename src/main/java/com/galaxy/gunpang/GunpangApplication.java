package com.galaxy.gunpang;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
public class GunpangApplication {

    public static void main(String[] args) {
        SpringApplication.run(GunpangApplication.class, args);
    }

}

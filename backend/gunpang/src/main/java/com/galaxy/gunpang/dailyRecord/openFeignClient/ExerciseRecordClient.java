package com.galaxy.gunpang.dailyRecord.openFeignClient;

import com.galaxy.gunpang.common.config.OpenFeignConfig;
import com.galaxy.gunpang.dailyRecord.openFeignClient.dto.ExerciseRecordResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "exerciseRecord", url = "http://3.35.214.166:8000", configuration = OpenFeignConfig.class)
public interface ExerciseRecordClient {

    @GetMapping(value = "/")
    List<ExerciseRecordResDto> getExerciseRecord(
        @RequestParam("playerId") String playerId,
        @RequestParam("date") String date,
        @RequestParam("age") int age,
        @RequestParam("gender") String gender
    );

}

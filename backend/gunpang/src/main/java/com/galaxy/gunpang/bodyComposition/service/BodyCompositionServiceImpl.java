package com.galaxy.gunpang.bodyComposition.service;

import com.galaxy.gunpang.bodyComposition.exception.BodyCompositionNotFoundException;
import com.galaxy.gunpang.bodyComposition.model.BodyComposition;
import com.galaxy.gunpang.bodyComposition.model.dto.BodyCompositionResDto;
import com.galaxy.gunpang.bodyComposition.repository.BodyCompositionRepository;
import com.galaxy.gunpang.dailyRecord.exception.DailyRecordNotFoundException;
import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.galaxy.gunpang.user.exception.UserNotFoundException;
import com.galaxy.gunpang.user.model.User;
import com.galaxy.gunpang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class BodyCompositionServiceImpl implements BodyCompositionService{

    private static final Logger logger = LoggerFactory.getLogger(BodyCompositionService.class);
    private final BodyCompositionRepository bodyCompositionRepository;
    private final UserRepository userRepository;
    @Override
    public void setBodyCompositionsWithHealthApi(Long userId, float weight, float muscleMass, float fatMass, float bodyWaterMass) {
        logger.debug("service");
        //체지방률
        float fatMassPct = ( fatMass / weight ) * 100;
        //만약 유저가 없으면 에러
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        BodyComposition bodyComposition = new BodyComposition(user,weight,muscleMass,fatMass,fatMassPct,bodyWaterMass);
        bodyCompositionRepository.save(bodyComposition);

    }

    @Override
    public BodyCompositionResDto getBodyCompositions(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new UserNotFoundException(userId));

        int userHeight = user.getHeight();
        List<BodyComposition> bodyCompositions = bodyCompositionRepository.findByUser(user);
        //체성분 기록이 없음
        if (bodyCompositions.isEmpty()){
            throw new BodyCompositionNotFoundException();
        }
        BodyCompositionResDto bodyCompositionResDto = null;
        float BMI = bodyCompositions.get(0).getWeight() / (userHeight * userHeight);
        //가장 최근 기록과 오늘과의 기간 계산
        Period period = Period.between(LocalDate.from(bodyCompositions.get(0).getCreatedAt()), LocalDate.now());
        //한 달 지난 기록만 있는 경우

        //한 달 이내 / 한달 이후 + 기록이 하나만
        if(bodyCompositions.size() == 1){
            //몸무게를 키의 제곱으로 나누기
            bodyCompositionResDto = BodyCompositionResDto.builder().curWeight(bodyCompositions.get(0).getWeight())
                    .curMuscleMass(bodyCompositions.get(0).getMuscleMass())
                    .curFatMass(bodyCompositions.get(0).getFatMass())
                    .curFatMassPct(bodyCompositions.get(0).getFatMassPct())
                    .curBodyWaterMass(bodyCompositions.get(0).getBodyWaterMass())
                    .curBMI(BMI).build();
            return bodyCompositionResDto;
        }
        //최근 기록이 존재하지만, 기록이 하나가 아님 (예전 기록이 존재)
        if(period.getMonths() < 1){
            float pastBMI = bodyCompositions.get(1).getWeight()/ (userHeight * userHeight);
            logger.debug(String.valueOf(bodyCompositions.get(1).getFatMass()));
            bodyCompositionResDto = BodyCompositionResDto.builder().curWeight(bodyCompositions.get(0).getWeight())
                    .prevWeight(bodyCompositions.get(1).getWeight())
                    .curMuscleMass(bodyCompositions.get(0).getMuscleMass())
                    .prevMuscleMass(bodyCompositions.get(1).getMuscleMass())
                    .curFatMass(bodyCompositions.get(0).getFatMass())
                    .prevFatMass(bodyCompositions.get(1).getFatMass())
                    .curFatMassPct(bodyCompositions.get(0).getFatMassPct())
                    .prevFatMassPct(bodyCompositions.get(1).getFatMassPct())
                    .curBodyWaterMass(bodyCompositions.get(0).getBodyWaterMass())
                    .prevBodyWaterMass(bodyCompositions.get(1).getBodyWaterMass())
                    .curBMI(BMI)
                    .prevBMI(pastBMI)
                    .build();
            return bodyCompositionResDto;
        }
        return bodyCompositionResDto;
    }
}

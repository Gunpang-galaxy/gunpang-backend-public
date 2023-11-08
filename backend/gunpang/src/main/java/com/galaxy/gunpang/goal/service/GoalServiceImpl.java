package com.galaxy.gunpang.goal.service;

import com.galaxy.gunpang.avatar.exception.AvatarAlreadyExistException;
import com.galaxy.gunpang.avatar.exception.AvatarNotFoundException;
import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.dto.AvatarIdReqDto;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
import com.galaxy.gunpang.common.model.enums.InitCode;
import com.galaxy.gunpang.goal.exception.GoalAlreadyExistException;
import com.galaxy.gunpang.goal.exception.GoalNotFoundException;
import com.galaxy.gunpang.goal.model.Goal;
import com.galaxy.gunpang.goal.model.dto.CalendarResDto;
import com.galaxy.gunpang.goal.model.dto.DamageResDto;
import com.galaxy.gunpang.goal.model.dto.GoalReqDto;
import com.galaxy.gunpang.goal.model.dto.GoalResDto;
import com.galaxy.gunpang.goal.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService{
    private final AvatarRepository avatarRepository;
    private final GoalRepository goalRepository;
    private final DeathCauseRepository deathCauseRepository;

    @Override
    public void addGoal(Long userId, GoalReqDto goalReqDto) {
        Long avatarId = avatarRepository.getCurIdByUserId(userId).orElseThrow(
                () -> new AvatarNotFoundException(InitCode.AVATAR_NOT_CREATED)
        );

        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow(
                AvatarNotFoundException::new
        );

        // 임시로 직접 repo에서 중복 avatarId 체크
        if(goalRepository.findByAvatar_Id(avatarId).isPresent()){
            throw new GoalAlreadyExistException(avatarId);
        }

        Goal goal = Goal.builder().avatar(avatar)
                .exerciseDay((byte)goalReqDto.getExerciseDay())
                .exerciseTime(goalReqDto.getExerciseTime())
                .sleepStartTime(LocalTime.of(goalReqDto.getStartTime(), goalReqDto.getStartMinute()))
                .sleepEndTime(LocalTime.of(goalReqDto.getEntTime(), goalReqDto.getEndMinute()))
                .build();
        goalRepository.save(goal);
    }

    @Override
    public GoalResDto getGoal(Long avatarId) {
        Goal goal = goalRepository.findByAvatar_Id(avatarId).orElseThrow(
                GoalNotFoundException::new
        );

        return GoalResDto.builder()
                .sleepStart(goal.getSleepStartTime().toString())
                .sleepEnd(goal.getSleepEndTime().toString())
                .exerciseDay(goal.getExerciseDay())
                .exerciseTime(goal.getExerciseTime())
                .build();
    }

    @Override
    public CalendarResDto getCalendar(Long userId, int year, int month) {
        List<DamageResDto> damageResDtoList = deathCauseRepository.findCalendar(userId, year, month);

        return CalendarResDto.builder().data(damageResDtoList).build();
    }
}

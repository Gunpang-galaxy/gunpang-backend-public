package com.galaxy.gunpang.goal.service;

import com.galaxy.gunpang.avatar.exception.AvatarNotFoundException;
import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.dto.AvatarIdReqDto;
import com.galaxy.gunpang.avatar.repository.AvatarRepository;
import com.galaxy.gunpang.avatar.repository.DeathCauseRepository;
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
                () -> new AvatarNotFoundException(userId)
        );

        Avatar avatar = avatarRepository.findById(avatarId).orElseThrow(
                ()-> new AvatarNotFoundException(avatarId)
        );
        Goal goal = Goal.builder().avatar(avatar)
                .exerciseDay((byte)goalReqDto.getExerciseDay())
                .exerciseTime(goalReqDto.getExerciseTime())
                .sleepStartTime(LocalTime.of(goalReqDto.getStartTime(), goalReqDto.getStartMinute()))
                .sleepEndTime(LocalTime.of(goalReqDto.getEntTime(), goalReqDto.getEndMinute()))
                .build();
        goalRepository.save(goal);
    }

    @Override
    public GoalResDto getGoal(AvatarIdReqDto avatarIdReqDto) {
        Goal goal = goalRepository.findByAvatar_Id(avatarIdReqDto.getAvatarId()).orElseThrow(
                () -> new GoalNotFoundException(avatarIdReqDto.getAvatarId())
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

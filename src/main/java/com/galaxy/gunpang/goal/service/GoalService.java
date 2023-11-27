package com.galaxy.gunpang.goal.service;

import com.galaxy.gunpang.goal.model.dto.CalendarResDto;
import com.galaxy.gunpang.goal.model.dto.GoalReqDto;
import com.galaxy.gunpang.goal.model.dto.GoalResDto;

public interface GoalService {
    public void addGoal(Long userId, GoalReqDto goalReqDto);
    public GoalResDto getGoal(Long avatarId);

    public CalendarResDto getCalendar(Long userId, int year, int month);
}

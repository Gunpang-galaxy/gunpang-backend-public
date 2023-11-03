package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.goal.model.dto.DamageResDto;

import java.util.List;

public interface DeathCauseRepositoryCustom {
    List<DamageResDto> findCalendar(Long userId, int year, int month);
}

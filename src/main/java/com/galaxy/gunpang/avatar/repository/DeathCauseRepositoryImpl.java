package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.goal.model.dto.DamageResDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.galaxy.gunpang.avatar.model.QDeathCause.deathCause;
import static com.galaxy.gunpang.avatar.model.QAvatar.avatar;

@RequiredArgsConstructor
public class DeathCauseRepositoryImpl implements DeathCauseRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<DamageResDto> findCalendar(Long userId, int year, int month) {
        // 해당 달의 유저 기록 중에 키운 아바타
        // 해당 아바타의 해당 달의 DeathCause count
        // 해당 DeathCause의
        return queryFactory.select(
                        Projections.constructor(DamageResDto.class, deathCause.date.dayOfMonth(), deathCause.count()))
                .from(deathCause)
                .innerJoin(avatar)
                .on(avatar.user.id.eq(userId).and(deathCause.avatar.id.eq(avatar.id)))
                .where(deathCause.date.year().eq(year).and(deathCause.date.month().eq(month)))
                .groupBy(deathCause.date.dayOfMonth())
                .fetch();
    }
}

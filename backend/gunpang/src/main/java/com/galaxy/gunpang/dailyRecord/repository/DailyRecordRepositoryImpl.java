package com.galaxy.gunpang.dailyRecord.repository;

import com.galaxy.gunpang.dailyRecord.model.DailyRecord;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

import static com.galaxy.gunpang.dailyRecord.model.QDailyRecord.dailyRecord;

@RequiredArgsConstructor
public class DailyRecordRepositoryImpl implements DailyRecordRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<DailyRecord> getDailyRecordOnTodayByUserId(Long userId, LocalDate date) {
        return Optional.ofNullable(queryFactory.selectFrom(dailyRecord)
                .where(dailyRecord.userId.eq(userId).and(dailyRecord.recordDate.eq(date)))
                .fetchFirst());
    }
}

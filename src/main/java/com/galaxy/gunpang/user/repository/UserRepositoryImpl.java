package com.galaxy.gunpang.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.galaxy.gunpang.user.model.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long getIdByGoogleId(String googleId) {
        return queryFactory.select(user.id)
                .from(user)
                .where(user.googleId.eq(googleId))
                .orderBy(user.id.desc())
                .fetchFirst();
    }

}
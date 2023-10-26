package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.avatar.model.AvatarType;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.galaxy.gunpang.avatar.model.QAvatarType.avatarType;

@RequiredArgsConstructor
public class AvatarTypeRepositoryImpl implements AvatarTypeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public AvatarType getAvatarTypeByRandom() {
        return queryFactory.selectFrom(avatarType)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .fetchFirst();
    }
}

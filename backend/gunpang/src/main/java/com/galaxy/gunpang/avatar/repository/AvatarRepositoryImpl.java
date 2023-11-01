package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.QAvatar;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.galaxy.gunpang.avatar.model.QAvatar.avatar;

@RequiredArgsConstructor
public class AvatarRepositoryImpl implements AvatarRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> getPrevIdByIdAndUserId(Long avatarId, Long userId) {
        return Optional.ofNullable(queryFactory.select(avatar.id).from(avatar)
                .where(avatar.user.id.eq(userId).and(avatar.id.gt(avatarId)))
                .orderBy(avatar.id.desc())
                .fetchFirst());
    }

    @Override
    public Optional<Long> getNextIdByIdAndUserId(Long avatarId, Long userId) {
        return Optional.ofNullable(queryFactory.select(avatar.id).from(avatar)
                .where(avatar.user.id.eq(userId).and(avatar.id.lt(avatarId)))
                .fetchFirst());
    }

    @Override
    public Optional<Long> getCurIdByUserId(Long userId) {
        return Optional.ofNullable(queryFactory.select(avatar.id).from(avatar)
                .where(avatar.user.id.eq(userId))
                .orderBy(avatar.id.desc())
                .fetchFirst());
    }

//    @Override
//    public List<Avatar> findAllAliveAvatar() {
//        return queryFactory.selectFrom(avatar)
//                .where(avatar.status.eq(Status.ALIVE)
//                ).fetch();
//    }
}

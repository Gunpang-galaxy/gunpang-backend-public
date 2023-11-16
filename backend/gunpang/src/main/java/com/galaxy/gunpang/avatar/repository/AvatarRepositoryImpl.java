package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.QAvatar;
import com.galaxy.gunpang.avatar.model.enums.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.galaxy.gunpang.avatar.model.QAvatar.avatar;

@Slf4j
@RequiredArgsConstructor
public class AvatarRepositoryImpl implements AvatarRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> getPrevIdByIdAndUserId(Long avatarId, Long userId) {
        return Optional.ofNullable(queryFactory.select(avatar.id).from(avatar)
                .where(avatar.user.id.eq(userId).and(avatar.id.lt(avatarId)))
                .fetchFirst());
    }

    @Override
    public Optional<Long> getNextIdByIdAndUserId(Long avatarId, Long userId) {
        return Optional.ofNullable(queryFactory.select(avatar.id).from(avatar)
                .where(avatar.user.id.eq(userId).and(avatar.id.gt(avatarId)))
                .orderBy(avatar.id.desc())
                .fetchFirst());
    }

    @Override
    public Optional<Long> getCurIdByUserId(Long userId) {
        return Optional.ofNullable(queryFactory.select(avatar.id).from(avatar)
                .where(avatar.user.id.eq(userId))
                .orderBy(avatar.id.desc())
                .fetchFirst());
    }

    @Override
    public Page<Avatar> findLevelUpAvatars(Pageable pageable) {
        LocalDate[] dates = new LocalDate[4];
        for(int i = 0; i < dates.length;) dates[i] = LocalDate.now().minusDays(7 * (++i));
        List<Avatar> avatars = queryFactory.selectFrom(avatar)
                .where(avatar.status.ne(Status.DEAD)
                        .and(avatar.startedDate.eq(dates[0])
                                .or(avatar.startedDate.eq(dates[1]))
                                .or(avatar.startedDate.eq(dates[2]))
                                .or(avatar.startedDate.eq(dates[3]))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        log.debug("avatars : {}", avatars.size());

        return new PageImpl<>(avatars, pageable, avatars.size());
    }

//    @Override
//    public List<Avatar> findAllAliveAvatar() {
//        return queryFactory.selectFrom(avatar)
//                .where(avatar.status.eq(Status.ALIVE)
//                ).fetch();
//    }
}

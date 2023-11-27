package com.galaxy.gunpang.bodyComposition.repository;

import com.galaxy.gunpang.bodyComposition.model.BodyComposition;
import com.galaxy.gunpang.bodyComposition.model.QBodyComposition;
import com.galaxy.gunpang.user.model.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RequiredArgsConstructor
public class BodyCompositionRepositoryImpl implements BodyCompositionRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(BodyCompositionRepositoryImpl.class);
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<BodyComposition>> getRecentTwoBodyCompositionByUser(User user) {
        logger.debug("BodyCompositionRepositoryImpl getRecentTwoBodyCompositionByUser");
        return Optional.ofNullable(queryFactory
                .selectFrom(QBodyComposition.bodyComposition)
                .where(QBodyComposition.bodyComposition.user.eq(user))
                .orderBy(QBodyComposition.bodyComposition.createdAt.desc())
                .limit(2)
                .fetch());
    }
}

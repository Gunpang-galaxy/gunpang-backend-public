package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.avatar.model.Avatar;

import java.util.Optional;

public interface AvatarRepositoryCustom {
    Optional<Long> getPrevIdByIdAndUserId(Long avatarId, Long userId);
    Optional<Long> getNextIdByIdAndUserId(Long avatarId, Long userId);
    Optional<Long> getCurIdByUserId(Long userId);
}

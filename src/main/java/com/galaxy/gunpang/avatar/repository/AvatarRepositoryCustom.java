package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.avatar.model.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AvatarRepositoryCustom {
    Optional<Long> getPrevIdByIdAndUserId(Long avatarId, Long userId);
    Optional<Long> getNextIdByIdAndUserId(Long avatarId, Long userId);
    Optional<Long> getCurIdByUserId(Long userId);

    Page<Avatar> findLevelUpAvatars(Pageable pageable);
//    List<Avatar> findAllAliveAvatar();
}

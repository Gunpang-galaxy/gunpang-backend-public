package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.avatar.model.Avatar;
import com.galaxy.gunpang.avatar.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> , AvatarRepositoryCustom{
    Optional<Avatar> findByIdAndUser_Id(Long avatarId, Long userId);
    Boolean existsByUser_IdAndStatus(Long user_id, Status status);
//    Optional<Long> getFirstIdByUser_IdOrderByIdDesc(Long userId);
    Page<Avatar> findByStatus(Status status, Pageable pageable);
}

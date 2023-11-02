package com.galaxy.gunpang.avatar.repository;

import com.galaxy.gunpang.avatar.model.DeathCause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeathCauseRepository extends JpaRepository<DeathCause, Long> {
    Optional<DeathCause> findFirstByAvatar_IdOrderByIdDesc(Long avatarId);
    List<DeathCause> findAllByAvatar_Id(Long avatarId);
}

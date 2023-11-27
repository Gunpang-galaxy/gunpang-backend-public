package com.galaxy.gunpang.goal.repository;

import com.galaxy.gunpang.goal.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findByAvatar_Id(Long avatarId);
}

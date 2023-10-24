package com.galaxy.gunpang.goal.repository;

import com.galaxy.gunpang.goal.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}

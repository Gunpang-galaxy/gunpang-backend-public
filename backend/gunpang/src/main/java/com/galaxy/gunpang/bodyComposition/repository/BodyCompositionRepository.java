package com.galaxy.gunpang.bodyComposition.repository;

import com.galaxy.gunpang.bodyComposition.model.BodyComposition;
import com.galaxy.gunpang.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodyCompositionRepository extends JpaRepository<BodyComposition, Long>, BodyCompositionRepositoryCustom {
    List<BodyComposition> findByUser(User user);

}
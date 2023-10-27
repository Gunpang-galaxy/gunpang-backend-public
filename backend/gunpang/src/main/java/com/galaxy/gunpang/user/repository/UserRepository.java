package com.galaxy.gunpang.user.repository;

import com.galaxy.gunpang.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

}
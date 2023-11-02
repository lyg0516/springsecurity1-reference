package com.cos.springsecurity1.repository;

import com.cos.springsecurity1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
}

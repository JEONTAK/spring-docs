package com.example.concurrent.domain.user.repository;

import com.example.concurrent.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

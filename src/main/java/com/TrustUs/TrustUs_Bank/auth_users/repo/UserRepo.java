package com.TrustUs.TrustUs_Bank.auth_users.repo;

import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
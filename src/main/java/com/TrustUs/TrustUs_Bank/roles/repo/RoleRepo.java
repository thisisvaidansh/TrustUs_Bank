package com.TrustUs.TrustUs_Bank.roles.repo;

import com.TrustUs.TrustUs_Bank.roles.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

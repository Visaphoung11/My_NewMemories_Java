package com.example.mymemories.repository;

import com.example.mymemories.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {  // ← Long, not UUID


    Optional<Role> findByName(String name);
}
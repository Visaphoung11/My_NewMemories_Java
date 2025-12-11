package com.example.mymemories.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mymemories.entity.Memory;
import com.example.mymemories.entity.User;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
	List<Memory> findByUser(User user);
}

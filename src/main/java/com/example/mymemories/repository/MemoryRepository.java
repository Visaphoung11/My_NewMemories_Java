package com.example.mymemories.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mymemories.entity.Memory;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
 
}

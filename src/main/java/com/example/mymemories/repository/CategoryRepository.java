package com.example.mymemories.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mymemories.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	boolean existsByName(String name);
}

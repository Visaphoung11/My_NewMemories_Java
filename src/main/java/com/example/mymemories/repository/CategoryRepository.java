package com.example.mymemories.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mymemories.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	boolean existsByName(String name);
	List<Category> findAllByNameIn(Collection<String> names);
}

package com.example.mymemories.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mymemories.entity.Memory;
import com.example.mymemories.entity.User;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
	@EntityGraph(attributePaths = {"imageList", "categories"})
	List<Memory> findAllByUser(User user);
	// Example: For your findById method (or similar method used in update/delete)
	@EntityGraph(attributePaths = {"imageList", "categories"})
	Optional<Memory> findById(Long id);

	// Note: If you don't have a findByUser method, you might need to create a custom one.
	// Example for fetching all memories for the authenticated user
	@Query("SELECT m FROM Memory m WHERE m.user = :user")
	@EntityGraph(attributePaths = {"imageList", "categories"})
	List<Memory> findMemoriesByUser(@Param("user") User user);

}

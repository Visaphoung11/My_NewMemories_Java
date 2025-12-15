package com.example.mymemories.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mymemories.entity.Memory;
import com.example.mymemories.entity.User;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    @EntityGraph(attributePaths = {"imageList", "categories"})
    List<Memory> findAllByUser(User user);

    @EntityGraph(attributePaths = {"imageList", "categories"})
    Optional<Memory> findById(Long id);

    @Query("SELECT m FROM Memory m WHERE m.user = :user")
    @EntityGraph(attributePaths = {"imageList", "categories"})
    List<Memory> findMemoriesByUser(@Param("user") User user);

    @EntityGraph(attributePaths = {"imageList", "categories"})
    Page<Memory> findAllByUser(User user, Pageable pageable);

}

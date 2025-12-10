package com.example.mymemories.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "memories")
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @CreationTimestamp // <-- Tells Hibernate to automatically populate this field on INSERT
    private Instant createdAt;

    @Column(name = "content") 
    private String description;
    
    // Default Constructor (REQUIRED by Hibernate)
    public Memory() {}

    // Custom Constructor for convenience (DTO -> Entity conversion)
    public Memory(String title, String description) {
        this.title = title;
        this.description = description;
        
    }

	



  
}
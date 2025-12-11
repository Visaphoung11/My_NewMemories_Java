package com.example.mymemories.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data; // Provides getters, setters, and constructors automatically

@Data // Important: Generates getId(), getTitle(), etc., resolving previous errors
@Entity
@Table(name = "memories")
public class Memory {

    // 1. PRIMARY KEY FIELD (Required by JPA and referenced by getId() in mapToDto)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. TIMESTAMPS FIELD (Required for getCreatedAt() in mapToDto)
    @CreationTimestamp
    private Instant createdAt;
    
    // 3. REGULAR FIELDS (Already included in your update)
    @Column(nullable = false) // Add column annotation for robustness
    private String title;
    
    @Column(name = "content", columnDefinition = "TEXT") // Use TEXT for potentially long content
    private String content;

    // 4. ENTITY RELATIONSHIP (The key fix)
    @ManyToOne // Many Memories belong to One User
    @JoinColumn(name = "user_id", nullable = false) // Defines the foreign key column in the 'memories' table
    private User user; // This MUST be the User entity class type

    
    // CONSTRUCTORS (Lombok's @Data handles NoArgsConstructor and AllArgsConstructor)
    
    // Custom Constructor for DTO -> Entity conversion (as used in MemoryService)
    public Memory(String title, String content) {
        this.title = title;
        this.content = content;
        // The 'user' relationship is set separately in the service: memory.setUser(user);
    }
}
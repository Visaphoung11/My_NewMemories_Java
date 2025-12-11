package com.example.mymemories.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode; // <--- NEW IMPORT
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.CascadeType;
@NoArgsConstructor
@Getter // Use @Getter and @Setter instead of @Data
@Setter
@Data 
@Entity
@Table(name = "memories")
public class Memory {

    // 1. PRIMARY KEY
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp

    private Instant createdAt;



    @Column(nullable = false)

    private String title;


    @Column(name = "content", columnDefinition = "TEXT") // Use TEXT for potentially long content

    private String content;
    // ... (createdAt, title, content fields) ...
    
    // 2. USER RELATIONSHIP (Many-to-One)
    @ManyToOne 
    @JoinColumn(name = "user_id", nullable = false) 
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user; 
    @EqualsAndHashCode.Exclude
    // 3. IMAGE RELATIONSHIP (One-to-Many)
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, orphanRemoval = true)
    // IMPORTANT: Initializing the collection is a good practice!
    private List<Image> imageList = new ArrayList<>(); 
    @EqualsAndHashCode.Exclude
    // 4. CATEGORY RELATIONSHIP (Many-to-Many)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
        CascadeType.PERSIST,  
        CascadeType.MERGE     
    })
    @JoinTable(
        name = "memory_categories", 
        joinColumns = @JoinColumn(name = "memory_id"), 
        inverseJoinColumns = @JoinColumn(name = "category_id") 
    )
    private Set<Category> categories = new HashSet<>(); // Must be here!



    public Memory(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
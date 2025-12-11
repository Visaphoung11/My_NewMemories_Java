package com.example.mymemories.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonIgnore; // <-- NEW IMPORT
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode; // <--- NEW IMPORT
@Entity
@Table(name = "categories")
@Getter
@Setter
@Data
@NoArgsConstructor
public class Category {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
    @JsonIgnore
    @EqualsAndHashCode.Exclude // <--- CRITICAL FIX 3: Exclude from hash/equals
    // The Many-to-Many relationship back to Memory (The non-owning side)
    @ManyToMany(mappedBy = "categories")
    private Set<Memory> memories;
}

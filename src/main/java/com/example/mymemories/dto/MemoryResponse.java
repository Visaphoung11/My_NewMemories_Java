package com.example.mymemories.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class MemoryResponse {
	private Long id;
    private String title;
    private String description;
    private String ownerUsername;
    private List<String> imageUrls;
    private Set<String> categoryNames;
	private Instant createdAt;
    

    // We can add a simple constructor to map from the Entity to the DTO
    public MemoryResponse(Long id, String title, String description, Instant createdAt, List<String> imageUrls, Set<String> categoryNames, String ownerUsername) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerUsername = ownerUsername;
        this.imageUrls = imageUrls;
        this.categoryNames = categoryNames;
        this.createdAt = createdAt;
    }
}

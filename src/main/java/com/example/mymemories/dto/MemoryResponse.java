package com.example.mymemories.dto;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class MemoryResponse {
	private Long id;
    private String title;
    private String description;
    private List<String> imageUrls;
	private Instant createdAt;
    

    // We can add a simple constructor to map from the Entity to the DTO
    public MemoryResponse(Long id, String title, String description, Instant createdAt, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrls = imageUrls;
        this.createdAt = createdAt;
    }
}

package com.example.mymemories.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.mymemories.entity.Category;
import com.example.mymemories.entity.Image;
import com.example.mymemories.entity.Memory;

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

 //  Entity → DTO mapper
    public static MemoryResponse from(Memory memory) {

        List<String> imageUrls = memory.getImageList()
                .stream()
                .map(Image::getImageUrl)
                .toList();

        Set<String> categoryNames = memory.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        return new MemoryResponse(
                memory.getId(),
                memory.getTitle(),
                memory.getContent(),          // or getDescription() if that's your field
                memory.getCreatedAt(),
                imageUrls,
                categoryNames,
                memory.getUser().getUsername()
        );
    }
}

package com.example.mymemories.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mymemories.dto.CreateMemoryRequest;
import com.example.mymemories.dto.MemoryResponse;
import com.example.mymemories.entity.Image;
import com.example.mymemories.entity.Memory;
import com.example.mymemories.entity.User;
import com.example.mymemories.repository.MemoryRepository;
import com.example.mymemories.repository.UserRepository;

@Service
public class MemoryService {
	private final MemoryRepository memoryRepository;
	private final UserRepository userRepository;
	
	
	public MemoryService(MemoryRepository memoryRepository, UserRepository userRepository) {
        this.memoryRepository = memoryRepository;
        this.userRepository = userRepository;
    }
	
	public Memory createMemory(CreateMemoryRequest request, String authenticatedUsername) {

	    User user = userRepository.findByUsername(authenticatedUsername)
	        .orElseThrow(() -> new RuntimeException("User not found: " + authenticatedUsername));
	        
	    // 2. Convert DTO to Entity
	    // *** CORRECTION: Only pass title and content to the constructor ***
	    Memory memory = new Memory(
	        request.getTitle(),
	        request.getContent() 
	        // authenticatedUsername REMOVED from constructor
	    );
	    
	    // 3. Set the User relationship (LINK THE MEMORY TO THE OWNER) - THIS IS PERFECT
	    memory.setUser(user); 
	    
	    if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
	        
	        List<Image> imageList = request.getImageUrls().stream()
	            // Create a new Image entity for each URL, linking it back to the Memory object
	            .map(url -> new Image(url, memory)) 
	            .collect(Collectors.toList());
	            
	        memory.setImageList(imageList);
	    }
	    
	    return memoryRepository.save(memory);
	}

	public List<MemoryResponse> getAllMemories() {
        return memoryRepository.findAll().stream()
                // Convert each Memory Entity to a MemoryResponse DTO
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
	
	private MemoryResponse mapToDto(Memory memory) {
	    
	    // Extract Image URLs from the entity list
	    List<String> imageUrls = memory.getImageList().stream()
	        .map(Image::getImageUrl) // Use the getter from the Image entity
	        .collect(Collectors.toList());
	        
	    return new MemoryResponse(
	        memory.getId(),
	        memory.getTitle(),
	        memory.getContent(),
	        memory.getCreatedAt(),
	        imageUrls 
	    );
	}
	
public Memory updateMemory(Long id, CreateMemoryRequest request, String authenticatedUsername) { // <-- ADD USERNAME
	    
	    // 1. Find the existing memory
	    Memory existingMemory = memoryRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Memory not found with ID: " + id));

	    // 2. AUTHORIZATION CHECK: Check if the memory belongs to the authenticated user
	    if (!existingMemory.getUser().getUsername().equals(authenticatedUsername)) { // <-- FIXED LINE
	        throw new SecurityException("User is not authorized to delete this memory.");
	    }
	    // Note: Use 'SecurityException' here, which the controller should handle (403 Forbidden).

	    // 3. Update the fields
	    existingMemory.setTitle(request.getTitle());
	    existingMemory.setContent(request.getContent());
	    
	    // 4. Save the updated entity
	    return memoryRepository.save(existingMemory);
	}

	// Inside MemoryService.java

	public void deleteMemory(Long id, String authenticatedUsername) { // <-- ADD USERNAME
	    
	    // 1. Find the existing memory
	    Memory existingMemory = memoryRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Memory not found with ID: " + id));
	    
	    // 2. AUTHORIZATION CHECK: Check if the memory belongs to the authenticated user
	    if (!existingMemory.getUser().getUsername().equals(authenticatedUsername)) { // <-- FIXED LINE
	        throw new SecurityException("User is not authorized to delete this memory.");
	    }
	    
	    // 3. Delete the memory
	    memoryRepository.deleteById(id);
	}


	
	public List<MemoryResponse> getAllMemoriesByUser(String authenticatedUsername) {
	 
	    User user = userRepository.findByUsername(authenticatedUsername)
	        .orElseThrow(() -> new RuntimeException("User not found: " + authenticatedUsername));
	        
	    
	    return memoryRepository.findByUser(user).stream() 
	        .map(this::mapToDto)
	        .collect(Collectors.toList());
	}


	public List<MemoryResponse> getAllMemoriesAdmin() { 
	    return memoryRepository.findAll().stream()
	        .map(this::mapToDto)
	        .collect(Collectors.toList());
	}

	
	
	
	
	
}
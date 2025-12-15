package com.example.mymemories.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.mymemories.dto.CreateMemoryRequest;
import com.example.mymemories.dto.ListResponseDTO;
import com.example.mymemories.dto.MemoryResponse;
import com.example.mymemories.entity.Category;
import com.example.mymemories.entity.Image;
import com.example.mymemories.entity.Memory;
import com.example.mymemories.entity.User;
import com.example.mymemories.exception.ResourceNotFoundException;
import com.example.mymemories.exception.UnauthorizedException;
import com.example.mymemories.repository.CategoryRepository;
import com.example.mymemories.repository.MemoryRepository;
import com.example.mymemories.repository.UserRepository;

import jakarta.transaction.Transactional;
@Transactional // <--- Apply to the whole class for all methods
@Service
public class MemoryService {
	private final MemoryRepository memoryRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	
	
	
	public MemoryService(MemoryRepository memoryRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.memoryRepository = memoryRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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
	    
	    // 3. Set the User relationship (LINK THE MEMORY TO THE OWNER)
	    memory.setUser(user); 
	    
	    if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
	        
	        List<Image> imageList = request.getImageUrls().stream()
	            // Create a new Image entity for each URL, linking it back to the Memory object
	            .map(url -> new Image(url, memory)) 
	            .collect(Collectors.toList());
	            
	        memory.setImageList(imageList);
	    }
	    if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
	        
	        Set<Long> requestedIds = request.getCategoryIds();
	        
	        // 1. Fetch the actual Category entities
	        List<Category> categoryList = categoryRepository.findAllById(requestedIds);

	        // 2. Validation Check (Recommended)
	        if (categoryList.size() != requestedIds.size()) {
	            // You would typically throw an exception here, like:
	            // throw new ResourceNotFoundException("One or more category IDs are invalid.");
	            
	            // For now, let's just log or proceed with valid categories
	        }
	        
	        // 3. Set the collection on the Memory entity
	        memory.setCategories(new HashSet<>(categoryList));
	    }
	    return memoryRepository.save(memory);
	}

	public List<MemoryResponse> getAllMemories() {
        return memoryRepository.findAll().stream()
                // Convert each Memory Entity to a MemoryResponse DTO
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
	
	public MemoryResponse mapToDto(Memory memory) { 
	    
	    // 1. Extract Image URLs (Existing Logic)
	    List<String> imageUrls = memory.getImageList().stream()
	        .map(Image::getImageUrl)
	        .collect(Collectors.toList());
	        
	    // 2. Extract Category Names 
	    Set<String> categoryNames = memory.getCategories().stream()
	        .map(Category::getName)
	        .collect(Collectors.toSet());
	    String username = memory.getUser().getUsername();
	    // 3. Return the DTO with all fields
	    return new MemoryResponse(
	        memory.getId(),
	        memory.getTitle(),
	        memory.getContent(),
	        memory.getCreatedAt(),
	        imageUrls,
	        categoryNames,username
	    );
	}
	
	public Memory updateMemory(Long id, CreateMemoryRequest request, String authenticatedUsername) {
	    
	    // 1. Find the existing memory (and implicitly load images/categories)
	    Memory existingMemory = memoryRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Memory not found with ID: " + id));

	    // 2. AUTHORIZATION CHECK (Security is perfect here)
	    if (!existingMemory.getUser().getUsername().equals(authenticatedUsername)) {
	        throw new SecurityException("User is not authorized to update this memory.");
	    }

	    // 3. Update CORE fields
	    existingMemory.setTitle(request.getTitle());
	    existingMemory.setContent(request.getContent());
	    
	    // A. Update Images (Delete existing, add new list)
	    if (request.getImageUrls() != null) {
	        
	        existingMemory.getImageList().clear(); 
	        
	        List<Image> newImageList = request.getImageUrls().stream()
	            .map(url -> new Image(url, existingMemory)) // Create new Image entity linked to memory
	            .collect(Collectors.toList());
	            
	        existingMemory.getImageList().addAll(newImageList);
	    }
	    
	    // B. Update Categories (Simple removal and re-adding of entities)
	    if (request.getCategoryIds() != null) {
	        // Clear old categories
	        existingMemory.getCategories().clear(); 
	        
	        // Fetch new Category entities from the database
	        Set<Long> requestedIds = request.getCategoryIds();
	        List<Category> newCategoryList = categoryRepository.findAllById(requestedIds);

	        existingMemory.getCategories().addAll(new HashSet<>(newCategoryList));
	    }
	    
	    // ----------------------------------------------------------------------

	    // 4. Save the updated entity
	    return memoryRepository.save(existingMemory);
	}
	

	public void deleteMemory(Long id, String authenticatedUsername) { // <-- ADD USERNAME
	    
	    // 1. Find the existing memory
	    Memory existingMemory = memoryRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Memory not found with ID: " + id));
	    
	    // 2. AUTHORIZATION CHECK: Check if the memory belongs to the authenticated user
	    if (!existingMemory.getUser().getUsername().equals(authenticatedUsername)) {
	        throw new SecurityException("User is not authorized to delete this memory.");
	    }
	    
	    // 3. Delete the memory
	    memoryRepository.deleteById(id);
	}

	public ListResponseDTO<MemoryResponse> getAllMemoriesByUser(String username, int page, int size) {
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found: " + username));

	    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

	    Page<Memory> memoryPage = memoryRepository.findAllByUser(user, pageable);

	    List<MemoryResponse> memoryResponses = memoryPage.getContent().stream()
	            .map(this::mapToDto)
	            .collect(Collectors.toList());

	    // Use full paginated constructor
	    return new ListResponseDTO<>(
	            true,
	            "Successfully retrieved user's memories.",
	            memoryResponses,
	            memoryPage.getNumber() + 1,
	            memoryPage.getSize(),
	            memoryPage.getTotalPages(),
	            memoryPage.getTotalElements(),
	            memoryPage.hasNext(),
	            memoryPage.hasPrevious()
	    );
	}


	public List<MemoryResponse> getAllMemoriesAdmin() { 
	    return memoryRepository.findAll().stream()
	        .map(this::mapToDto)
	        .collect(Collectors.toList());
	}
	// Return type to MemoryResponse
	public MemoryResponse getMemoryById (Long id, String authenticatedUsername) {
		// Logic to find memoryById
		 Memory memory = memoryRepository.findById(id)
			        .orElseThrow(() -> new ResourceNotFoundException("Memory not found with this ID: " + id));
		// 2. Authorization check (same logic as update)
		 if(!memory.getUser().getUsername().equals(authenticatedUsername)) {
			 throw new UnauthorizedException("User is not authorized to view this memory");
		 }
		 // 3. Map Entity → DTO using constructor
		 return MemoryResponse.from(memory);
		
	}
	
}
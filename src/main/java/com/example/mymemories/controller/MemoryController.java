package com.example.mymemories.controller;

import java.security.Principal; // REQUIRED IMPORT for getting user identity
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mymemories.dto.CreateMemoryRequest;
import com.example.mymemories.dto.ListResponseDTO;
import com.example.mymemories.dto.MemoryCreationResponse;
import com.example.mymemories.dto.MemoryResponse;
import com.example.mymemories.entity.Memory;
import com.example.mymemories.service.MemoryService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {
	private final MemoryService memoryService;
	
	public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    // CREATE MEMORY (Requires token and links memory to user)
	@PostMapping
    public ResponseEntity<MemoryCreationResponse> createMemory(
        @RequestBody CreateMemoryRequest request,
        Principal principal // <-- Captures authenticated user from JWT
    ) {

        
		String authenticatedUsername = principal.getName();
        
       
		Memory newMemory = memoryService.createMemory(request, authenticatedUsername);
        
		MemoryCreationResponse response = new MemoryCreationResponse(
                true,
                "Memory created successfully!",
                newMemory
            );
        
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

    // GET ALL MEMORIES (Requires token and retrieves only the user's memories)
	@GetMapping
    public ResponseEntity<ListResponseDTO<MemoryResponse>> getAllMemories(
        Principal principal // <-- Captures authenticated user
    ) {
        String authenticatedUsername = principal.getName();
        
        // Call service method to filter memories by user
        List<MemoryResponse> memories = memoryService.getAllMemoriesByUser(authenticatedUsername); 
        
        ListResponseDTO<MemoryResponse> response = new ListResponseDTO<>(
                true, // Success flag
                "Successfully retrieved user's memories.",
                memories
            );
            
        // 200 OK is standard for successful GET requests
        return ResponseEntity.ok(response); 
    }
	
	// Simplified MemoryController.java
	// ...
	@PutMapping("/{id}")
	public ResponseEntity<MemoryCreationResponse> updateMemory(
	    @PathVariable Long id,
	    @RequestBody CreateMemoryRequest request,
	    Principal principal
	) {
	    String authenticatedUsername = principal.getName();
	    
	    // If updateMemory throws SecurityException or RuntimeException, the global handler catches it
	    Memory updatedMemory = memoryService.updateMemory(id, request, authenticatedUsername); 
	    
	    MemoryCreationResponse response = new MemoryCreationResponse(
	        true,
	        "Memory updated successfully!",
	        updatedMemory
	    );
	    
	    return ResponseEntity.ok(response);
	}
	// ...

    // DELETE MEMORY (Requires token and verifies ownership before deleting)
    @DeleteMapping("/{id}")
    public ResponseEntity<MemoryCreationResponse> deleteMemory(
        @PathVariable Long id,
        Principal principal // <-- Captures authenticated user
    ) {
        try {
            String authenticatedUsername = principal.getName();

            // Pass the user ID for ownership check inside the service
            memoryService.deleteMemory(id, authenticatedUsername); 
            
            MemoryCreationResponse response = new MemoryCreationResponse(
                true,
                "Memory deleted successfully!",
                null // Memory object is null since it was deleted
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (SecurityException e) {
             // Service throws SecurityException if user doesn't own the memory
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            // Handle the "Memory not found" exception
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
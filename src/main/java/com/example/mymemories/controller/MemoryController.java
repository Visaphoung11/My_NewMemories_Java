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
import org.springframework.web.bind.annotation.RequestParam;
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
	    
	    // 1. Get the authenticated username
	    String authenticatedUsername = principal.getName();
	    
	    // 2. Call service layer to create and persist the raw JPA entity
	    Memory newMemory = memoryService.createMemory(request, authenticatedUsername);
	    
	   
	    // Ensure memoryService.mapToDto is public
	    MemoryResponse memoryDto = memoryService.mapToDto(newMemory); 
	    
	    // 3. Create the response object using the SAFE DTO
	    // This resolves the "unresolved variable" error and the future serialization error
	    MemoryCreationResponse response = new MemoryCreationResponse(
	            true,
	            "Memory created successfully!",
	            memoryDto // <-- This variable is now correctly defined
	        );
	    
	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

    // GET ALL MEMORIES (Requires token and retrieves only the user's memories)
	@GetMapping
	public ResponseEntity<ListResponseDTO<MemoryResponse>> getAllMemories(
	        Principal principal,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	) {
	    ListResponseDTO<MemoryResponse> response = memoryService.getAllMemoriesByUser(principal.getName(), page, size);
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
	    
	    Memory updatedMemory = memoryService.updateMemory(id, request, authenticatedUsername); 
	    
	    // 🛑 CRITICAL STEP: Convert the JPA entity to the DTO
	    MemoryResponse memoryDto = memoryService.mapToDto(updatedMemory); 

	    // Use the updated DTO structure
	    MemoryCreationResponse response = new MemoryCreationResponse(
	        true,
	        "Memory updated successfully!",
	        memoryDto // <-- Pass the DTO (MemoryResponse)
	    );
	    
	    return ResponseEntity.ok(response);
	}
	// Get memory by id best practice use try catch
//	@GetMapping("/{id}")
//	public ResponseEntity<ListResponseDTO<MemoryResponse>> getSingleMemory(
//	        @PathVariable Long id,
//	        Principal principal
//	) {
//	    try {
//	        String authenticatedUsername = principal.getName();
//	        MemoryResponse memoryDto = memoryService.getMemoryById(id, authenticatedUsername);
//	        List<MemoryResponse> dataList = List.of(memoryDto);
//
//	        return ResponseEntity.ok(
//	                new ListResponseDTO<>(true, "Memory fetched successfully", dataList)
//	        );
//
//	    } catch (RuntimeException e) {
//	        // Return failed response with 404 or 403
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                .body(new ListResponseDTO<>(false, e.getMessage(), List.of()));
//	    }
//	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<ListResponseDTO<MemoryResponse>> getSingleMemory(
	        @PathVariable Long id,
	        Principal principal
	) {
	    MemoryResponse memoryDto =
	            memoryService.getMemoryById(id, principal.getName());

	    return ResponseEntity.ok(
	            new ListResponseDTO<>(
	                    true,
	                    "Memory fetched successfully",
	                    List.of(memoryDto)
	            )
	    );
	}


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
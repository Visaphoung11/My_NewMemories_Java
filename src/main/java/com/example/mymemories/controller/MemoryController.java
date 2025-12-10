package com.example.mymemories.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	@PostMapping
    public ResponseEntity<MemoryCreationResponse> createMemory(@RequestBody CreateMemoryRequest request) {

        // 1. Call the Service logic, which now returns the full Memory Entity
        Memory newMemory = memoryService.createMemory(request);
        
        // 2. Wrap the result with a message DTO
        MemoryCreationResponse response = new MemoryCreationResponse(
                true, 
                "Memory created successfully!",
                newMemory
            );
        
        // 3. Return 201 CREATED status with the structured response
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping 
    public ResponseEntity<ListResponseDTO<MemoryResponse>> getAllMemories() {
        List<MemoryResponse> memories = memoryService.getAllMemories();
        
        ListResponseDTO<MemoryResponse> response = new ListResponseDTO<>(
                true, // Success flag
                "Successfully retrieved all memories.", 
                memories
            );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

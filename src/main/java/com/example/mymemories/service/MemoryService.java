package com.example.mymemories.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mymemories.dto.CreateMemoryRequest;
import com.example.mymemories.dto.MemoryResponse;
import com.example.mymemories.entity.Memory;
import com.example.mymemories.repository.MemoryRepository;

@Service
public class MemoryService {
	private final MemoryRepository memoryRepository;
	
	public MemoryService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }
	
	public Memory createMemory(CreateMemoryRequest request) {
        // 1. Convert DTO to Entity
        Memory memory = new Memory(
            request.getTitle(),
            request.getDescription()
        );
         Memory savedMemory = memoryRepository.save(memory);
        
      
         return savedMemory;
	}
	
	public List<MemoryResponse> getAllMemories() {
        return memoryRepository.findAll().stream()
                // Convert each Memory Entity to a MemoryResponse DTO
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
	private MemoryResponse mapToDto(Memory memory) {
        // Use the constructor we created in the DTO
        return new MemoryResponse(
            memory.getId(),
            memory.getTitle(),
            memory.getDescription(),
            memory.getCreatedAt()
        );
    }
	
	public Memory updateMemory(Long id, CreateMemoryRequest request) {
	    // 1. Find the existing memory, or throw an exception (404 Not Found)
	    Memory existingMemory = memoryRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Memory not found with ID: " + id));

	    // 2. Update the fields
	    existingMemory.setTitle(request.getTitle());
	    existingMemory.setDescription(request.getDescription());
	    
	    // 3. Save the updated entity
	    return memoryRepository.save(existingMemory);
	}

	public void deleteMemory(Long id) {
	    // 1. Check if the memory exists (optional, but gives better error handling)
	    if (!memoryRepository.existsById(id)) {
	        throw new RuntimeException("Memory not found with ID: " + id);
	    }
	    
	    // 2. Delete the memory
	    memoryRepository.deleteById(id);
	}
	
	
	
	
	
}
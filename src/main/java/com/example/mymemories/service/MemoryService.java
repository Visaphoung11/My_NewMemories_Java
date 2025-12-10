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
	
	
	
	
	
	
	
	
}
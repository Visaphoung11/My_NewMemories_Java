package com.example.mymemories.dto;

import lombok.Data;
@Data
public class MemoryCreationResponse {
	private boolean success;
	private String message;
	private MemoryResponse createdMemory;    
    public MemoryCreationResponse(boolean success, String message, MemoryResponse newMemory) {
        this.success = success;
        this.message = message;
        this.createdMemory = newMemory;
    }
    
}

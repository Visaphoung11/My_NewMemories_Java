package com.example.mymemories.dto;

import com.example.mymemories.entity.Memory;


import lombok.Data;
@Data
public class MemoryCreationResponse {
	private boolean success;
	private String message;
    private Memory createdMemory;
    
    public MemoryCreationResponse(boolean success, String message, Memory createdMemory) {
        this.success = success;
        this.message = message;
        this.createdMemory = createdMemory;
    }
    
}

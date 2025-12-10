package com.example.mymemories.dto;

import lombok.Data;

@Data
public class CreateMemoryRequest {
	private String title;
    private String description;
}

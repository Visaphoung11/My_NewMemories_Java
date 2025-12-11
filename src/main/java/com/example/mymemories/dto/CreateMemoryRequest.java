package com.example.mymemories.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateMemoryRequest {
	private String title;
    private String content;
    private List<String> imageUrls;
}

package com.example.mymemories.dto;

import java.util.List;


import lombok.Data;

@Data

public class ListResponseDTO<T> {
	private boolean success;
    private String message;
    private List<T> data; // The list of items being returned
    private int count;    // Optional: How many items are in the list

    // Manual constructor for safety, just like before:
    public  ListResponseDTO(boolean success, String message, List<T> data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.count = data.size();
    }
}

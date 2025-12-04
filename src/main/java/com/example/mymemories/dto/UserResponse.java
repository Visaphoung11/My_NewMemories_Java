package com.example.mymemories.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor  
@Data
@AllArgsConstructor
public class UserResponse {

	private Long id;
//    private String fullName;
    private String email;
    private String username;
    private String password;   
    private boolean enabled;
    private String createdAt;
    private String updatedAt;
}

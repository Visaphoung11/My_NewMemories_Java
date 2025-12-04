package com.example.mymemories.controller;

import com.example.mymemories.dto.ApiResponse;
import com.example.mymemories.dto.AuthResponse;
import com.example.mymemories.dto.LoginRequest;
import com.example.mymemories.dto.LoginResponse;
import com.example.mymemories.dto.RegisterRequest;
import com.example.mymemories.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

   
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest req) {
        ApiResponse<AuthResponse> response = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



 
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResponse response = authService.login(req);
        return ResponseEntity.ok(response);
    }
}
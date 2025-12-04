package com.example.mymemories.service;

import com.example.mymemories.dto.AuthResponse;
import com.example.mymemories.dto.LoginRequest;
import com.example.mymemories.dto.LoginResponse;
import com.example.mymemories.dto.RegisterRequest;
import com.example.mymemories.dto.UserResponse;
import com.example.mymemories.entity.Role;
import com.example.mymemories.entity.User;
import com.example.mymemories.repository.RoleRepository;
import com.example.mymemories.repository.UserRepository;
import com.example.mymemories.security.JwtProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;


    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));

        User saved = userRepo.save(user);

        String token = jwtProvider.generateToken(saved.getUsername(), saved.getId());

        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setMessage("User registered successfully");
        res.setId(saved.getId());
//        res.setFullName(saved.getUsername()); 
        res.setEmail(saved.getEmail());
        res.setUsername(saved.getUsername());
        res.setEnabled(saved.isEnabled());
        res.setCreatedAt(saved.getCreatedAt().toString());
        res.setUpdatedAt(saved.getUpdatedAt().toString());

        return res;
    }


    // LOGIN METHOD 
    public LoginResponse login(LoginRequest req) {
        // Step 1: Find user by email
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Step 2: Compare password correctly
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Step 3: Generate JWT
        String token = jwtProvider.generateToken(
                user.getUsername(),
                user.getId()
                
           
               
        );
        System.out.println("LoginRequest email: " + req.getEmail());
        System.out.println("LoginRequest password: " + req.getPassword());
        LoginResponse bongchang = new LoginResponse();
        bongchang.setToken(token);
        bongchang.setMessage("User logined successfully");
        return bongchang;

//        return new AuthResponse(token, token, null, token, token, false, token, token);
    }
     
}
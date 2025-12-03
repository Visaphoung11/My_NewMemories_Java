package com.example.mymemories.service;

import com.example.mymemories.dto.AuthResponse;
import com.example.mymemories.dto.LoginRequest;
import com.example.mymemories.dto.RegisterRequest;
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

//    public AuthService(UserRepository userRepo,
//                       RoleRepository roleRepo,
//                       PasswordEncoder encoder,
//                       JwtProvider jwtProvider) {
//        this.userRepo = userRepo;
//        this.roleRepo = roleRepo;
//        this.encoder = encoder;
//        this.jwtProvider = jwtProvider;
//    }

    // REGISTER METHOD - FIXED
    public AuthResponse register(RegisterRequest req) {
        // Check duplicates
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        // Create user
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));

        // Save and return token
        userRepo.save(user);

        String token = jwtProvider.generateToken(
                user.getUsername(),
                user.getId()
        );

        return new AuthResponse(token, token);
    }

    // LOGIN METHOD - COMPLETELY FIXED
    public AuthResponse login(LoginRequest req) {
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

        return new AuthResponse(token, token);
    }
}
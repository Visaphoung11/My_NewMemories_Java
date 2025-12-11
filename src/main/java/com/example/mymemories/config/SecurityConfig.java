package com.example.mymemories.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.mymemories.security.JwtAuthenticationFilter;

@Configuration 
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	// I injected JwtAuthenticationFilter and create Constructor
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
	    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
   
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        // 1. MUST BE HERE TO STOP THE 403 ERROR
	        .csrf(csrf -> csrf.disable()) // 
	        
	        
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        
	        // 3. Register the custom filter
	        .addFilterBefore(
	            jwtAuthenticationFilter, 
	            UsernamePasswordAuthenticationFilter.class
	        )

	        // 4. Define authorization rules
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/auth/**").permitAll()
	            .requestMatchers("/api/memories/**").authenticated()
	            .anyRequest().authenticated()
	        );

	    return http.build(); 
	}

    // 2. PASSWORD ENCODER BEAN
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. AUTHENTICATION MANAGER BEAN
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
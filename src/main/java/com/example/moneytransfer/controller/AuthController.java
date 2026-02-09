package com.example.moneytransfer.controller;

import com.example.moneytransfer.dto.AuthResponse;
import com.example.moneytransfer.dto.LoginRequest;
import com.example.moneytransfer.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${security.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal);

        AuthResponse response = AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(expirationSeconds)
                .build();

        return ResponseEntity.ok(response);
    }
}


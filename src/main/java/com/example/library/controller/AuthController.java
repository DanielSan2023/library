package com.example.library.controller;

import com.example.library.dto.jwtrequests.LoginRequestDto;
import com.example.library.dto.jwtrequests.RegisterRequestDto;
import com.example.library.entity.UserInfo;
import com.example.library.filter.JwtUtil;
import com.example.library.service.service.UserInfoService;
import com.example.library.utility.BookConstants;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("prod")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoService userInfoService;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserInfoService userInfoService) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userInfoService = userInfoService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto request) {
        if (userInfoService.findUserByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        UserInfo userInfo = UserInfo.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(BookConstants.DEFAULT_CUSTOMER_ROLE)
                .build();

        userInfoService.saveUser(userInfo);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto request) {
        return userInfoService.findUserByEmail(request.email())
                .map(customer -> {
                    boolean matches = passwordEncoder.matches(request.password(), customer.getPassword());

                    if (matches) {
                        String token = jwtUtil.generateToken(customer.getCustomerId(), customer.getRole());
                        return ResponseEntity.ok(token);
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                    }
                })
                .orElseGet(() -> {
                    System.out.println("Email not found: " + request.email());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                });
    }
}

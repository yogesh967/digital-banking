package com.bank.digital_banking.controller;

import com.bank.digital_banking.dto.AuthResponse;
import com.bank.digital_banking.dto.UserLoginRequest;
import com.bank.digital_banking.dto.UserRegistrationRequest;
import com.bank.digital_banking.entity.User;
import com.bank.digital_banking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserRegistrationRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody UserLoginRequest request) {
        return userService.login(request);
    }
}

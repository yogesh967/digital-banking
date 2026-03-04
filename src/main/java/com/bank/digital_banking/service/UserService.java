package com.bank.digital_banking.service;

import com.bank.digital_banking.dto.AuthResponse;
import com.bank.digital_banking.dto.UserLoginRequest;
import com.bank.digital_banking.dto.UserRegistrationRequest;
import com.bank.digital_banking.entity.User;
import com.bank.digital_banking.repository.UserRepository;
import com.bank.digital_banking.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserRegistrationRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public AuthResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException(("Invalid credentials"));
        }
        String token = JwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}

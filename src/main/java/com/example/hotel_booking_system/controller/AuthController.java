package com.example.hotel_booking_system.controller;

import com.example.hotel_booking_system.common.builder.ResponseBuilder;
import com.example.hotel_booking_system.common.dto.ResponseDto;
import com.example.hotel_booking_system.common.enumeration.ResponseCode;
import com.example.hotel_booking_system.dto.request.LoginRequest;
import com.example.hotel_booking_system.dto.request.RegisterRequest;
import com.example.hotel_booking_system.dto.response.AuthResponse;
import com.example.hotel_booking_system.dto.response.UserResponse;
import com.example.hotel_booking_system.service.AuthService;
import com.example.hotel_booking_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = userService.register(request);
        return ResponseBuilder.success(userResponse, ResponseCode.SUCCESS);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseBuilder.success(authResponse, ResponseCode.SUCCESS);
    }
}

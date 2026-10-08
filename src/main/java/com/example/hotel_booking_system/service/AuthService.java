package com.example.hotel_booking_system.service;

import com.example.hotel_booking_system.common.enumeration.ResponseCode;
import com.example.hotel_booking_system.dto.request.LoginRequest;
import com.example.hotel_booking_system.dto.response.AuthResponse;
import com.example.hotel_booking_system.entity.RefreshToken;
import com.example.hotel_booking_system.entity.User;
import com.example.hotel_booking_system.exception.BusinessException;
import com.example.hotel_booking_system.mapper.UserMapper;
import com.example.hotel_booking_system.repository.RefreshTokenRepository;
import com.example.hotel_booking_system.repository.UserRepository;
import com.example.hotel_booking_system.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;


    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException(ResponseCode.USERNAME_PASSWORD_MISS_MATCH));

        if(!user.getIsActive()){
            throw new BusinessException(ResponseCode.ACCOUNT_LOCKED);
    }
        Boolean matches = passwordEncoder.matches(loginRequest.getPassword(),user.getPasswordHash());
        if(!matches){
            throw new BusinessException(ResponseCode.USERNAME_PASSWORD_MISS_MATCH);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userMapper.toResponse(user))
                .build();

    }
}

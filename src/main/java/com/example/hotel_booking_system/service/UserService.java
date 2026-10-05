package com.example.hotel_booking_system.service;

import com.example.hotel_booking_system.common.enumeration.ResponseCode;
import com.example.hotel_booking_system.dto.request.RegisterRequest;
import com.example.hotel_booking_system.dto.request.UserRequest;
import com.example.hotel_booking_system.dto.response.UserResponse;
import com.example.hotel_booking_system.entity.Role;
import com.example.hotel_booking_system.entity.User;

import com.example.hotel_booking_system.exception.BusinessException;
import com.example.hotel_booking_system.mapper.UserMapper;
import com.example.hotel_booking_system.repository.RoleRepository;
import com.example.hotel_booking_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ap.shaded.freemarker.core.ReturnInstruction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponse> search(UserRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<User> users = userRepository.search(request.getKeyword(), pageable);
        return users.map(userMapper::toResponse);
    }


    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ResponseCode.EMAIL_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName("ROLE_GUEST")
                .orElseThrow(() -> new BusinessException(ResponseCode.ENTITY_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(encodedPassword)
                .fullName(request.getFullName())
                .phone(request.getPhoneNumber())
                .roles(Set.of(role))
                .build();
        User saveUser = userRepository.save(user);
        return userMapper.toResponse(saveUser);
    }

}

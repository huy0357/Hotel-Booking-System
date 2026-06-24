package com.example.hotel_booking_system.service;

import com.example.hotel_booking_system.dto.request.UserRequest;
import com.example.hotel_booking_system.dto.response.UserResponse;
import com.example.hotel_booking_system.entity.User;

import com.example.hotel_booking_system.mapper.UserMapper;
import com.example.hotel_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<UserResponse> search(UserRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<User> users = userRepository.search(request.getKeyword(), pageable);
        return users.map(userMapper::toResponse);
    }
}

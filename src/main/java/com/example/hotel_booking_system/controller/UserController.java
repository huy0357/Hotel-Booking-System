package com.example.hotel_booking_system.controller;

import com.example.hotel_booking_system.common.builder.ResponseBuilder;
import com.example.hotel_booking_system.common.dto.ResponseDto;
import com.example.hotel_booking_system.common.entity.MetaData;
import com.example.hotel_booking_system.common.enumeration.ResponseCode;
import com.example.hotel_booking_system.dto.request.UserRequest;
import com.example.hotel_booking_system.dto.response.UserResponse;
import com.example.hotel_booking_system.entity.User;
import com.example.hotel_booking_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping
    public ResponseEntity<ResponseDto<List<UserResponse>>> search(@ModelAttribute UserRequest request) {
        Page<UserResponse> responsePage = userService.search(request);
        MetaData metaData =MetaData.builder()
                .currentPage(request.getPage())
                .totalPage(responsePage.getTotalPages())
                .totalElements(responsePage.getTotalElements())
                .build();
        return ResponseBuilder.success(
                responsePage.getContent(),
                ResponseCode.SUCCESS,
                metaData
        );
    }

}

    package com.example.hotel_booking_system.mapper;

    import com.example.hotel_booking_system.dto.response.UserResponse;
    import com.example.hotel_booking_system.entity.User;
    import org.mapstruct.Mapper;


    @Mapper(componentModel = "spring")
    public interface UserMapper {

        UserResponse toResponse(User user);
    }

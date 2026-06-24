        package com.example.hotel_booking_system.dto.response;

        import lombok.Data;

        @Data
        public class  UserResponse {
                private Long id;
                private String fullName;
                private String email;
                private String phone;
                private Boolean isActive;

        }

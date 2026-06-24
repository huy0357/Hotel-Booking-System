package com.example.hotel_booking_system.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Builder.Default
    private String keyword = "";
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    private Boolean isVisible;
}

package com.example.hotel_booking_system.common.dto;

import com.example.hotel_booking_system.common.entity.MetaData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private String message; // final message trả ra
    private T data;
    private Integer statusCode;
    private MetaData metaData;

    // ===== INTERNAL (không expose ra ngoài) =====
    @JsonIgnore
    private String messageKey;

    @JsonIgnore
    private Object[] args;
}

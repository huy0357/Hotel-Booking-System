package com.example.hotel_booking_system.exception;

import com.example.hotel_booking_system.common.enumeration.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ResponseCode responseCode;
    private final transient Object[] args;

    public BusinessException(ResponseCode responseCode, Object... args) {
        super(responseCode.getMessageKey());
        this.responseCode = responseCode;
        this.args = args;
    }
}
package com.example.hotel_booking_system.common.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(0, "success", HttpStatus.OK),

    // ===== AUTH / SECURITY (14xx) =====
    UNAUTHORIZED(1401, "error.auth.unauthorized", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1403, "error.auth.access-denied", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(1404, "error.auth.token-expired", HttpStatus.UNAUTHORIZED),
    AUTHORIZATION_DENIED(1405, "error.auth.authorization-denied", HttpStatus.FORBIDDEN),
    INVALID_API_KEY(1406, "error.auth.invalid-api-key", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED(1407, "error.auth.account-locked", HttpStatus.FORBIDDEN),
    USERNAME_PASSWORD_MISS_MATCH(1408, "error.auth.username-password-mismatch", HttpStatus.UNAUTHORIZED),

    // ===== VALIDATION (2xxx) =====
    VALIDATION_FAILED(2000, "error.validation.failed", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER(2001, "error.validation.invalid-parameter", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER(2002, "error.validation.missing-parameter", HttpStatus.BAD_REQUEST),
    TYPE_MISMATCH(2003, "error.validation.type-mismatch", HttpStatus.BAD_REQUEST),
    MALFORMED_JSON(2004, "error.validation.malformed-json", HttpStatus.BAD_REQUEST),
    JSON_MAPPING_ERROR(2005, "error.validation.json-mapping", HttpStatus.BAD_REQUEST),

    // ===== REQUEST / HTTP (3xxx) =====
    METHOD_NOT_SUPPORTED(3000, "error.http.method-not-supported", HttpStatus.METHOD_NOT_ALLOWED),
    MEDIA_TYPE_NOT_SUPPORTED(3001, "error.http.media-type-not-supported", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    BAD_REQUEST(3002, "error.http.bad-request", HttpStatus.BAD_REQUEST),

    // ===== BUSINESS (4xxx) =====
    BUSINESS_ERROR(4000, "error.business.general", HttpStatus.BAD_REQUEST),
    NOT_FOUND(4004, "error.resource.not-found", HttpStatus.NOT_FOUND),
    ENTITY_NOT_FOUND(4005, "error.entity.not-found", HttpStatus.NOT_FOUND),
    ENTITY_DELETED(4006, "error.entity.deleted", HttpStatus.GONE),
    ENTITY_INACTIVE(4007, "error.entity.inactive", HttpStatus.CONFLICT),
    INVALID_STATE(4008, "error.entity.invalid-state", HttpStatus.CONFLICT),
    ENTITY_NOT_DELETABLE(4009, "error.entity.not-deletable", HttpStatus.CONFLICT),

    // hotel_booking_system
    EMAIL_ALREADY_EXISTS(4010, "error.user.email-already-exists", HttpStatus.CONFLICT),
    ROOM_NOT_AVAILABLE(4011, "error.booking.room-not-available", HttpStatus.CONFLICT),
    INVALID_DATE_RANGE(4012, "error.booking.invalid-date-range", HttpStatus.BAD_REQUEST),
    BOOKING_CANNOT_BE_CANCELLED(4013, "error.booking.cannot-cancel", HttpStatus.CONFLICT),
    PROMOTION_INVALID(4014, "error.promotion.invalid", HttpStatus.BAD_REQUEST),

    // ===== SYSTEM / FATAL (5xxx) =====
    SOMETHING_WENT_WRONG(5000, "error.fatal.something-went-wrong", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR(5001, "error.fatal.internal-server-error", HttpStatus.INTERNAL_SERVER_ERROR),
    TIMEOUT(5002, "error.system.timeout", HttpStatus.GATEWAY_TIMEOUT),

    // ===== THIRD PARTY (6xxx) =====
    THIRD_PARTY_ERROR(6000, "error.third-party.general", HttpStatus.BAD_GATEWAY);

    private final int statusCode;
    private final String messageKey;
    private final HttpStatus httpStatus;
}
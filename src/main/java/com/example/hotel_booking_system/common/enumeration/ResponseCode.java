package com.example.hotel_booking_system.common.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(0, "success"),

    // ===== AUTH / SECURITY (1000) =====

    UNAUTHORIZED(1401, "error.auth.unauthorized"),
    ACCESS_DENIED(1403, "error.auth.access-denied"),
    TOKEN_EXPIRED(1404, "error.auth.token-expired"),
    AUTHORIZATION_DENIED(1405, "error.auth.authorization-denied"),
    INVALID_API_KEY(1406, "error.auth.invalid-api-key"),
    ACCOUNT_LOCKED(1407, "error.auth.account-locked"),
    USERNAME_PASSWORD_MISS_MATCH(1408, "error.auth.username-password-mismatch"),

    // ===== VALIDATION (2000) =====

    VALIDATION_FAILED(2000, "error.validation.failed"),
    INVALID_PARAMETER(2001, "error.validation.invalid-parameter"),
    MISSING_PARAMETER(2002, "error.validation.missing-parameter"),
    TYPE_MISMATCH(2003, "error.validation.type-mismatch"),
    MALFORMED_JSON(2004, "error.validation.malformed-json"),
    JSON_MAPPING_ERROR(2005, "error.validation.json-mapping"),

    // ===== REQUEST / HTTP (3000) =====

    METHOD_NOT_SUPPORTED(3000, "error.http.method-not-supported"),
    MEDIA_TYPE_NOT_SUPPORTED(3001, "error.http.media-type-not-supported"),
    BAD_REQUEST(3002, "error.http.bad-request"),

    // ===== BUSINESS (4000) =====

    BUSINESS_ERROR(4000, "error.business.general"),
    NOT_FOUND(4004, "error.resource.not-found"),
    ENTITY_NOT_FOUND(4005, "error.entity.not-found"),
    ENTITY_DELETED(4006, "error.entity.deleted"),
    ENTITY_INACTIVE(4007, "error.entity.inactive"),
    INVALID_STATE(4008, "error.entity.invalid-state"),
    ENTITY_NOT_DELETABLE(4009, "error.entity.not-deletable"),
    NOTIFICATION_BROADCAST_CONTAIN_USER_VAR(4010, "error.notification.broadcast-contain-user-variable"),
    SURVEY_ALREADY_COMPLETED(4011, "error.survey.already-completed"),

    // ===== THIRD PARTY (6000) =====

    THIRD_PARTY_ERROR(6000, "error.third-party.general"),

    // ===== SYSTEM / FATAL (5000) =====

    SOMETHING_WENT_WRONG(5000, "error.fatal.something-went-wrong"),
    INTERNAL_SERVER_ERROR(5001, "error.fatal.internal-server-error"),
    TIMEOUT(5002, "error.system.timeout");

    private final int statusCode;
    private final String messageKey;

}

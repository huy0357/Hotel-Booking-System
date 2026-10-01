package com.example.hotel_booking_system.exception;

import com.example.hotel_booking_system.common.dto.ResponseDto;
import com.example.hotel_booking_system.common.enumeration.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // ===== Lỗi nghiệp vụ =====
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDto<Object>> handleBusiness(BusinessException ex) {
        log.warn("Business error {}: {}", ex.getResponseCode().getStatusCode(), ex.getMessage());
        return build(ex.getResponseCode(), null, ex.getArgs());
    }

    // ===== Validation =====
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> Map.of(
                        "field", e.getField(),
                        "message", String.valueOf(e.getDefaultMessage())))
                .toList();
        return build(ResponseCode.VALIDATION_FAILED, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON: {}", ex.getMessage());
        return build(ResponseCode.MALFORMED_JSON, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(ResponseCode.TYPE_MISMATCH, null, ex.getName());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDto<Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return build(ResponseCode.MISSING_PARAMETER, null, ex.getParameterName());
    }

    // ===== HTTP =====
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return build(ResponseCode.METHOD_NOT_SUPPORTED, null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseDto<Object>> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        return build(ResponseCode.MEDIA_TYPE_NOT_SUPPORTED, null);
    }

    // URL không tồn tại (Spring Boot 3.2+ ném lỗi này; nếu thiếu handler sẽ bị trả nhầm 500)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseDto<Object>> handleNoResource(NoResourceFoundException ex) {
        return build(ResponseCode.NOT_FOUND, null);
    }

    // ===== Security (cần khi dùng @PreAuthorize) =====
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDto<Object>> handleAccessDenied(AccessDeniedException ex) {
        return build(ResponseCode.ACCESS_DENIED, null);
    }

    // ===== Lưới an toàn cuối: không lộ chi tiết lỗi ra client =====
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return build(ResponseCode.INTERNAL_SERVER_ERROR, null);
    }

    private <T> ResponseEntity<ResponseDto<T>> build(ResponseCode code, T data, Object... args) {
        String message = messageSource.getMessage(
                code.getMessageKey(), args, code.getMessageKey(), LocaleContextHolder.getLocale());

        ResponseDto<T> response = ResponseDto.<T>builder()
                .success(false)
                .statusCode(code.getStatusCode())
                .message(message)
                .data(data)
                .build();

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }
}
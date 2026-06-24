package com.example.hotel_booking_system.common.builder;

import com.example.hotel_booking_system.common.dto.ResponseDto;
import com.example.hotel_booking_system.common.entity.MetaData;
import com.example.hotel_booking_system.common.enumeration.ResponseCode;
import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@UtilityClass
public class ResponseBuilder {

    // ===== SUCCESS =====

    public static <T> ResponseEntity<ResponseDto<T>> success(
            T data,
            ResponseCode code
    ) {
        return build(true, null, data, code, null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> success(
            T data,
            ResponseCode code,
            Object... args
    ) {
        return build(true, args, data, code, null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> success(
            T data,
            ResponseCode code,
            MetaData metaData
    ) {
        return build(true, null, data, code, metaData, HttpStatus.OK);
    }

    // ===== ERROR =====

    public static <T> ResponseEntity<ResponseDto<T>> error(
            ResponseCode code
    ) {
        return build(false, null, null, code, null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> error(
            ResponseCode code,
            Object... args
    ) {
        return build(false, args, null, code, null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> error(
            T data,
            ResponseCode code
    ) {
        return build(false, null, data, code, null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> error(
            ResponseCode code,
            String message
    ) {

        return build(false, null, (T) message, code, null, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ResponseDto<T>> error(
            ResponseCode code,
            HttpStatus status
    ) {
        return build(false, null, null, code, null, status);
    }

    // ===== CORE BUILD =====

    private static <T> ResponseEntity<ResponseDto<T>> build(
            boolean success,
            Object[] args,
            T data,
            ResponseCode code,
            MetaData metaData,
            HttpStatus status
    ) {

        ResponseDto<T> dto = ResponseDto.<T>builder()
                .success(success)
                .args(args)
                .data(data)
                .statusCode(code.getStatusCode())
                .metaData(metaData)
                .build();

        return ResponseEntity.status(status).body(dto);
    }

    // ===== DOWNLOAD =====

    @Nonnull
    public static ResponseEntity<InputStreamResource> download(
            @Nonnull InputStreamResource resource,
            @Nonnull String fileName) {

        return ResponseEntity.ok()
                .contentType(APPLICATION_OCTET_STREAM)
                .header(CONTENT_DISPOSITION,
                        "attachment; filename=" + fileName)
                .body(resource);
    }
}

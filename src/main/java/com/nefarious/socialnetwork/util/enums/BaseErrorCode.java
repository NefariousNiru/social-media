package com.nefarious.socialnetwork.util.enums;

import com.nefarious.socialnetwork.util.interfaces.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseErrorCode implements ErrorCode {
    VALIDATION_FAILED           ("Validation failed",           HttpStatus.BAD_REQUEST),
    ;
    private final String message;
    private final HttpStatus httpStatus;
}

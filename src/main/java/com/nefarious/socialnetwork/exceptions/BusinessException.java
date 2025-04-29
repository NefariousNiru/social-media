package com.nefarious.socialnetwork.exceptions;

import com.nefarious.socialnetwork.util.interfaces.ErrorCode;
import lombok.Getter;

/**
 * Custom runtime exception to handle business logic errors across the application.
 *
 * <p>Encapsulates an {@link ErrorCode} containing the error message and HTTP status,
 * allowing consistent exception handling across different modules.
 */
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

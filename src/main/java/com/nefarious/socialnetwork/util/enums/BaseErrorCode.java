package com.nefarious.socialnetwork.util.enums;

import com.nefarious.socialnetwork.util.interfaces.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseErrorCode implements ErrorCode {
    TOO_MANY_ATTEMPTS           ("You went too fast and hit a wall. Try later!", HttpStatus.TOO_MANY_REQUESTS),
    VALIDATION_FAILED           ("Validation failed",           HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR       ("Oh we ran into a hiccup!",    HttpStatus.INTERNAL_SERVER_ERROR)
    ;
    private final String message;
    private final HttpStatus httpStatus;
}

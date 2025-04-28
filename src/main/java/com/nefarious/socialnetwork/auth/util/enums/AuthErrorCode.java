package com.nefarious.socialnetwork.auth.util.enums;

import com.nefarious.socialnetwork.util.interfaces.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    EMAIL_NOT_VERIFIED          ("Email is not verified",       HttpStatus.FORBIDDEN),
    EMAIL_IN_USE                ("Email already in use",        HttpStatus.BAD_REQUEST),
    USERNAME_IN_USE             ("Username already in use",     HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS             ("User does not exist",         HttpStatus.BAD_REQUEST),
    FAILED_TO_SEND_OTP          ("Failed to send OTP",          HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CREDENTIALS         ("Invalid credentials",         HttpStatus.UNAUTHORIZED),
    INVALID_OTP                 ("Invalid or Expired OTP",      HttpStatus.BAD_REQUEST),
    ;
    private final String message;
    private final HttpStatus httpStatus;
}

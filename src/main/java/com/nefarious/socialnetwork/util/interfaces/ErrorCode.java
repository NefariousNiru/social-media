package com.nefarious.socialnetwork.util.interfaces;

import org.springframework.http.HttpStatus;

/** Inherit with Error Enums only */
public interface ErrorCode {
    /** Get Error Message corresponding to the ErrorCode */
    String getMessage();
    /** Get {@link HttpStatus} corresponding to the ErrorCode */
    HttpStatus getHttpStatus();
}

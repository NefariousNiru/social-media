package com.nefarious.socialnetwork.exceptions;


import com.nefarious.socialnetwork.auth.dto.BusinessError;
import com.nefarious.socialnetwork.util.interfaces.ErrorCode;
import com.nefarious.socialnetwork.util.enums.BaseErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Handler for your custom business exceptions */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessError> handleBusiness(BusinessException ex) {
        ErrorCode code = ex.getErrorCode();
        BusinessError body = new BusinessError(code, code.getMessage(), System.currentTimeMillis());
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(body);
    }

    /** Handle @Valid bean validation failures */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BusinessError> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        BusinessError body = new BusinessError(BaseErrorCode.VALIDATION_FAILED, errors, System.currentTimeMillis());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    /** Fallback for any other unhandled exception */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BusinessError> handleAll(Exception ex, HttpServletRequest req) {
        BusinessError body = new BusinessError(
                BaseErrorCode.VALIDATION_FAILED,
                "An unexpected error occurred",
                System.currentTimeMillis()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}

package com.nefarious.socialnetwork.auth.dto;

import com.nefarious.socialnetwork.util.interfaces.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/** This class is used to return standardized error messages in JSON format to the client. */
@Data
@AllArgsConstructor
public class BusinessError {
    private ErrorCode errorCode;
    private String message;
    private long timestamp;
}

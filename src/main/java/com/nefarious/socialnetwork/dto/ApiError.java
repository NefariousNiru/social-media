package com.nefarious.socialnetwork.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** This class is used to return standardized error messages in JSON format to the client. */
@Data
@AllArgsConstructor
public class ApiError {
    private String message;
}

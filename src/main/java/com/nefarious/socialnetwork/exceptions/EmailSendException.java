package com.nefarious.socialnetwork.exceptions;

/**
 * Thrown when an e-mail cannot be sent.
 */
public class EmailSendException extends RuntimeException {
    public EmailSendException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

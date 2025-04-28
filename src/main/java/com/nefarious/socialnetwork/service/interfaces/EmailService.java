package com.nefarious.socialnetwork.service.interfaces;


import com.nefarious.socialnetwork.exceptions.EmailSendException;

/**
 * Send e-mails within the app.
 */
public interface EmailService {
    /**
     * Send a one-time password (OTP) to the given address.
     *
     * @param to      recipient e-mail address
     * @param otpCode the 10-char alphanumeric code
     * @throws EmailSendException if there is an error while sending the email.
     */
    void sendOtpEmail(String to, String otpCode) throws EmailSendException;
}

package com.nefarious.socialnetwork.service;

import com.nefarious.socialnetwork.entity.OtpEntry;
import com.nefarious.socialnetwork.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;

    private static final String ALPHANUMERIC = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
    private static final int OTP_LENGTH = 10;
    private final SecureRandom random = new SecureRandom();


    /** Generate a 10-char OTP and store in Redis,
     * @param email: E-Mail to send OTP to.
     */
    public String generateAndSaveOtp(String email) {
        String code = this.generateOtp();
        OtpEntry entry = OtpEntry.builder()
                .email(email)
                .code(code)
                .build();
        otpRepository.save(entry);
        return code;
    }

    /** Validate OTP, and delete on success
     * @param email: Email of user
     * @param otp: otp of user
     */
    public boolean validateOtp(String email, String otp) {
        return otpRepository.findById(email)
                .map(otpEntry -> {
                    boolean ok = otpEntry.getCode().equals(otp);
                    if (ok) otpRepository.delete(otpEntry);
                    return ok;
                })
                .orElse(false);
    }

    /** Generates a fixed 10-char alphanumeric OTP */
    private String generateOtp(){
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            int idx = random.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(idx));
        }
        return sb.toString();
    }
}

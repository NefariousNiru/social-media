package com.nefarious.socialnetwork.auth.service;

import com.nefarious.socialnetwork.exceptions.BusinessException;
import com.nefarious.socialnetwork.auth.service.interfaces.EmailService;
import com.nefarious.socialnetwork.auth.util.enums.AuthErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;


    @Override
    public void sendOtpEmail(String to, String otpCode) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("Your Verification Code");
            String html = """
                <p>Hello,</p>
                <p>Your one-time code is <b>%s</b></p>
                <p>This code expires in 10 minutes.</p>
                """.formatted(otpCode);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error("Failed to send OTP to {}: {}", from, ex.getMessage());
            throw new BusinessException(AuthErrorCode.FAILED_TO_SEND_OTP);
        }
    }
}

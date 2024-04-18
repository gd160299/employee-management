package com.example.employee_management.Service;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OTPService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;
    private final Map<String, String> userSecrets = new HashMap<>();

    public String generateOTP(String username) {
        String secretKey = this.userSecrets.computeIfAbsent(username, k -> Base32.random());
        Totp totp = new Totp(secretKey);
        return totp.now();
    }
    
    @Async
    public void sendOtpViaEmail(String toEmail, String otp) {
        String subject = "Your OTP Code";
        String content = "Your OTP code is: " + otp + ". Please do not share this code with anyone. \nNote: The OTP will expire after 1 minute.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.emailUsername);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);

    }
}


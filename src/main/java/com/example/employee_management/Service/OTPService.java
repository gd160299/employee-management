package com.example.employee_management.Service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
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

    public void sendOtpViaEmail(String toEmail, String otp) {
        String subject = "Your OTP Code";
        String content = "Your OTP code is: " + otp + ". Please do not share this code with anyone.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.emailUsername); // Sử dụng địa chỉ email của bạn
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);

    }
}


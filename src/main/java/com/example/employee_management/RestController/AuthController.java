package com.example.employee_management.RestController;

import com.example.employee_management.Model.JwtResponse;
import com.example.employee_management.Model.LoginRequest;
import com.example.employee_management.Model.OtpVerifyRequest;
import com.example.employee_management.Service.AuthService;
import com.example.employee_management.Service.JwtService;
import com.example.employee_management.Service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = authService.authenticateEmployee(
                loginRequest.getUsername(), loginRequest.getPassword());

        if (isAuthenticated) {
            // Nếu xác thực thành công, gửi thông báo OTP đã được gửi
            return ResponseEntity.ok("OTP sent to the email.");
        } else {
            // Nếu xác thực thất bại, trả về lỗi
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest otpVerifyRequest) {
        boolean isOtpValid = authService.verifyOtp(
                otpVerifyRequest.getUsername(), otpVerifyRequest.getOtp());

        if (isOtpValid) {
            // OTP đúng, sinh và trả về JWT token
            String token = jwtService.generateToken(otpVerifyRequest.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            // OTP sai, trả về lỗi
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
        }
    }

    @PostMapping("/regenerate-otp")
    public ResponseEntity<?> regenerateOtp(@RequestBody OtpVerifyRequest otp) {
        authService.regenerateOtp(otp.getUsername());
        return ResponseEntity.ok("A new OTP has been sent.");
    }

}

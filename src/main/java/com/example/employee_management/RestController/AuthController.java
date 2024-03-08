package com.example.employee_management.RestController;

import com.example.employee_management.Model.JwtResponse;
import com.example.employee_management.Model.LoginRequest;
import com.example.employee_management.Model.OtpVerifyRequest;
import com.example.employee_management.Service.AuthService;
import com.example.employee_management.Service.JwtService;
import com.example.employee_management.Util.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        // Create a response body, could be a Map or a custom DTO
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getErrorCode().toString());
        response.put("message", ex.getMessage());

        // You can choose an appropriate HTTP status
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // API này dùng để xác thực người dùng, truyền vào username và password
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = this.authService.authenticateEmployee(
                loginRequest.getUsername(), loginRequest.getPassword());

        if (isAuthenticated) {
            // Nếu xác thực thành công, gửi thông báo OTP đã được gửi
            return ResponseEntity.ok("OTP sent to the email.");
        } else {
            // Nếu xác thực thất bại, trả về lỗi
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    // API này dùng để lấy thông tin vai trò của người dùng, truyền vào username
    @GetMapping("get-roles")
    public ResponseEntity<?> getRoles(@RequestParam String username) {
        List<String> roles = this.authService.getUserRoles(username);
        return ResponseEntity.ok(roles);
    }

    // API này dùng để xác thực OTP, truyền vào username và mã OTP, response trả về JWT token bao gồm thông tin vai trò và username
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest otpVerifyRequest) {
        boolean isOtpValid = this.authService.verifyOtp(
                otpVerifyRequest.getUsername(), otpVerifyRequest.getOtp());

        if (isOtpValid) {
            // Lấy thông tin vai trò của người dùng
            List<String> roles = this.authService.getUserRoles(otpVerifyRequest.getUsername());
            // OTP đúng, sinh và trả về JWT token
            String token = jwtService.generateToken(otpVerifyRequest.getUsername(), roles);
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            // OTP sai, trả về lỗi
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP.");
        }
    }

    // API này dùng để gửi lại mã OTP, truyền vào username
    @PostMapping("/regenerate-otp")
    public ResponseEntity<?> regenerateOtp(@RequestBody OtpVerifyRequest otp) {
        this.authService.regenerateOtp(otp.getUsername());
        return ResponseEntity.ok("A new OTP has been sent.");
    }

}

package com.example.employee_management.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerifyRequest {
    private String username;
    private String otp;
}

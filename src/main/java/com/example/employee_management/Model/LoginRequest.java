package com.example.employee_management.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String oldPassword;
    private String password;
}

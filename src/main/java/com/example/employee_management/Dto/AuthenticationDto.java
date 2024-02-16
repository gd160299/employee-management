package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class AuthenticationDto {
    @DbColumnMapper("Auth_ID")
    private Long authId;

    @DbColumnMapper("Employee_ID")
    private Long employeeId;

    @DbColumnMapper("User_name")
    private String userName;

    @DbColumnMapper("OTP")
    private String otp;

    @DbColumnMapper("Last_Login")
    private Date lastLogin;

    @DbColumnMapper("created_time")
    private Date createdTime;
}

package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EmployeeDto {
    @DbColumnMapper("Employee_ID")
    private Long employeeId;

    @DbColumnMapper("Employee_Name")
    private String employeeName;

    @DbColumnMapper("Department_ID")
    private Long departmentId;

    @DbColumnMapper("Salary")
    private BigDecimal salary;

    @DbColumnMapper("User_name")
    private String userName;

    @DbColumnMapper("PHONE_NUMBER")
    private String phoneNumber;

    @DbColumnMapper("Position")
    private String position;

    @DbColumnMapper("EMAIL")
    private String email;

    @DbColumnMapper("PASS_WORD")
    private String passWord;
}

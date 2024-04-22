package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Data;

@Data
public class EmployeeDeviceDto {
    @DbColumnMapper("Employee_ID")
    private Long employeeId;

    @DbColumnMapper("MAC")
    private String mac;

    @DbColumnMapper("STATUS")
    private Long status;

}

package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePermissionsDto {
    @DbColumnMapper("Employee_ID")
    private Long employeeId;

    @DbColumnMapper("Permission_ID")
    private Long permissionId;
}

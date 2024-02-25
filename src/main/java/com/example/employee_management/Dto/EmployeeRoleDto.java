package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Data;

@Data
public class EmployeeRoleDto {
    @DbColumnMapper("EMPLOYEE_ID")
    private Long employeeId;

    @DbColumnMapper("ROLE_ID")
    private Long roleId;

    @DbColumnMapper("ROLE_NAME")
    private String roleName;
}

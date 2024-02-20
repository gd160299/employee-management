package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRoleDto {
    @DbColumnMapper("EMPLOYEE_ID")
    private Long employeeId;

    @DbColumnMapper("ROLE_ID")
    private Long roleId;
}

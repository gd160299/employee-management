package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentAccessDto {
    @DbColumnMapper("Access_ID")
    private Long accessId;

    @DbColumnMapper("Department_ID")
    private Long departmentId;

    @DbColumnMapper("Permission_ID")
    private Long permissionId;
}

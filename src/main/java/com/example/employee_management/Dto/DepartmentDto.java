package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Data;

@Data
public class DepartmentDto {
    @DbColumnMapper("Department_ID")
    private Long departmentId;

    @DbColumnMapper("Department_Name")
    private String departmentName;

}

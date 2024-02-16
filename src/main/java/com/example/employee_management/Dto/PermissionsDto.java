package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionsDto {
    @DbColumnMapper("Permission_ID")
    private Long permissionId;

    @DbColumnMapper("Permission_Name")
    private String permissionName;

    @DbColumnMapper("Description")
    private String description;
}

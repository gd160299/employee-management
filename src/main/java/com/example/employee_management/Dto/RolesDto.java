package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesDto {
    @DbColumnMapper("ROLE_ID")
    private Long roleId;

    @DbColumnMapper("ROLE_NAME")
    private String roleName;
}

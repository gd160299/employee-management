package com.example.employee_management.Repo;

import com.example.employee_management.Common.CallStoredProcedureCommon;
import com.example.employee_management.Dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EmployeeRepository {
    @Autowired
    private CallStoredProcedureCommon storedProcedureUtil;

    public Optional<EmployeeDto> findByUserName(String userName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_user_name", userName);
        EmployeeDto result = storedProcedureUtil.callStoredProcedureForSingleResult("PKG_EMPLOYEE.FIND_EMPLOYEE_BY_USER_NAME", parameters, EmployeeDto.class);
        return Optional.ofNullable(result);
    }
}

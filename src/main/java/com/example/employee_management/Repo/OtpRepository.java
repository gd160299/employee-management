package com.example.employee_management.Repo;

import com.example.employee_management.Common.CallStoredProcedureCommon;
import com.example.employee_management.Dto.AuthenticationDto;
import com.example.employee_management.Dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Repository
public class OtpRepository {
    @Autowired
    private CallStoredProcedureCommon storedProcedureUtil;

    public Optional<AuthenticationDto> findByUserName(String userName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_user_name", userName);
        AuthenticationDto result = storedProcedureUtil.callStoredProcedureForSingleResult("PKG_EMPLOYEE.FIND_EMPLOYEE_OTP_BY_USER_NAME", parameters, AuthenticationDto.class);
        return Optional.ofNullable(result);
    }

    public void delete(Long id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_id", id);
        storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.DELETE_AUTH_OTP", parameters);
    }

    public void save(AuthenticationDto dto) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_employee_id", dto.getEmployeeId());
        parameters.put("p_user_name", dto.getUserName());
        parameters.put("p_otp", dto.getOtp());
        parameters.put("p_created_time", dto.getCreatedTime());
        storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.SAVE_AUTH_OTP", parameters);
    }
}

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
        EmployeeDto result = this.storedProcedureUtil.callStoredProcedureForSingleResult("PKG_EMPLOYEE.FIND_EMPLOYEE_BY_USER_NAME", parameters, EmployeeDto.class);
        return Optional.ofNullable(result);
    }

    public void create(EmployeeDto objInput) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_employee_name", objInput.getEmployeeName());
        parameters.put("p_department_id", objInput.getDepartmentId());
        parameters.put("p_salary", objInput.getSalary());
        parameters.put("p_user_name", objInput.getUserName());
        parameters.put("p_phone_number", objInput.getPhoneNumber());
        parameters.put("p_position", objInput.getPosition());
        parameters.put("p_email", objInput.getEmail());
        parameters.put("p_pass_word", objInput.getPassWord());
        this.storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.save_employee", parameters);
    }

    public void update(EmployeeDto objInput) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_employee_id", objInput.getEmployeeId());
        parameters.put("p_employee_name", objInput.getEmployeeName());
        parameters.put("p_department_id", objInput.getDepartmentId());
        parameters.put("p_salary", objInput.getSalary());
        parameters.put("p_user_name", objInput.getUserName());
        parameters.put("p_phone_number", objInput.getPhoneNumber());
        parameters.put("p_position", objInput.getPosition());
        parameters.put("p_email", objInput.getEmail());
        parameters.put("p_pass_word", objInput.getPassWord());
        this.storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.update_employee", parameters);
    }

}
